package com.example.huertohogartiendaapp // <-- ESTA DEBE SER LA PRIMERA LÍNEA EXACTA

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogartiendaapp.data.FirebaseManager
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.data.ProductoRepository
import com.example.huertohogartiendaapp.util.ValidateLoginInput
import com.example.huertohogartiendaapp.util.ValidateRegistrationInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// --- DEFINICIÓN DE CLASES DE ESTADO ---
data class CarritoItem(
    val producto: Producto,
    val cantidad: Int
)

data class AppUiState(
    val productosEnCarrito: List<CarritoItem> = emptyList(),
    val snackbarMessage: String? = null,
    val verduras: List<Producto> = emptyList(),
    val frutas: List<Producto> = emptyList(),
    val isLoadingProductos: Boolean = false,
    val registerUsername: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerConfirmPassword: String = "",
    val registrationSuccess: Boolean = false,
    val loginEmailOrUsername: String = "",
    val loginPassword: String = "",
    val loginSuccess: Boolean = false,
    val fotoPerfilUri: Uri? = null,
    val isLoading: Boolean = false,
    // Propiedades para la pantalla de Dirección
    val direccion: String = "",
    val ciudad: String = "",
    val region: String = "",
    val codigoPostal: String = "",
    val direccionGuardadaConExito: Boolean = false
) {
    val totalCarrito: Double
        get() = productosEnCarrito.sumOf { it.producto.precio * it.cantidad }
}

// --- CLASE PRINCIPAL DEL VIEWMODEL ---

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    private val productoRepository = ProductoRepository()
    private val validateRegistrationInput = ValidateRegistrationInput()
    private val validateLoginInput = ValidateLoginInput()

    // --- LÓGICA DE CARGA CON FLOW ---
    fun cargarProductos() {
        // Establecemos una "tubería" que escucha los cambios en Firebase
        productoRepository.getTodosLosProductosEnTiempoReal()
            .onEach { productosDesdeFirebase ->
                // Este bloque se ejecuta CADA VEZ que hay una actualización
                _uiState.update {
                    it.copy(
                        verduras = productosDesdeFirebase.filter { p -> p.categoria == "Verduras" },
                        frutas = productosDesdeFirebase.filter { p -> p.categoria == "Frutas" },
                        isLoadingProductos = false // Actualizamos el estado de carga
                    )
                }
            }
            .launchIn(viewModelScope) // Inicia la recolección del Flow en el ciclo de vida del ViewModel
    }

    // --- LÓGICA DEL CARRITO ---
    fun agregarAlCarrito(producto: Producto) {
        _uiState.update { currentState ->
            val itemExistente = currentState.productosEnCarrito.find { it.producto.id == producto.id }
            if (itemExistente == null) {
                if (producto.stock > 0) {
                    val nuevoCarrito = currentState.productosEnCarrito + CarritoItem(producto, 1)
                    currentState.copy(productosEnCarrito = nuevoCarrito, snackbarMessage = "'${producto.nombre}' agregado al carrito")
                } else {
                    currentState.copy(snackbarMessage = "'${producto.nombre}' está agotado")
                }
            } else {
                if (itemExistente.cantidad < producto.stock) {
                    val nuevoCarrito = currentState.productosEnCarrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                    currentState.copy(productosEnCarrito = nuevoCarrito, snackbarMessage = "Cantidad de '${producto.nombre}' actualizada")
                } else {
                    currentState.copy(snackbarMessage = "No hay más stock de '${producto.nombre}'")
                }
            }
        }
    }

    fun incrementarCantidad(productoId: String) {
        _uiState.update { currentState ->
            val nuevoCarrito = currentState.productosEnCarrito.map { item ->
                if (item.producto.id == productoId && item.cantidad < item.producto.stock) {
                    item.copy(cantidad = item.cantidad + 1)
                } else {
                    item
                }
            }
            currentState.copy(productosEnCarrito = nuevoCarrito)
        }
    }

    fun decrementarCantidad(productoId: String) {
        _uiState.update { currentState ->
            val itemADecrementar = currentState.productosEnCarrito.find { it.producto.id == productoId }
            if (itemADecrementar != null && itemADecrementar.cantidad > 1) {
                val nuevoCarrito = currentState.productosEnCarrito.map { item ->
                    if (item.producto.id == productoId) item.copy(cantidad = item.cantidad - 1) else item
                }
                currentState.copy(productosEnCarrito = nuevoCarrito, snackbarMessage = "Cantidad actualizada")
            } else {
                eliminarProducto(productoId)
                currentState
            }
        }
    }

    fun eliminarProducto(productoId: String) {
        _uiState.update { currentState ->
            val itemAEliminar = currentState.productosEnCarrito.find { it.producto.id == productoId }
            currentState.copy(
                productosEnCarrito = currentState.productosEnCarrito.filterNot { it.producto.id == productoId },
                snackbarMessage = "'${itemAEliminar?.producto?.nombre ?: "Producto"}' eliminado del carrito"
            )
        }
    }

    // --- LÓGICA DE AUTENTICACIÓN Y DIRECCIÓN ---
    fun onRegisterClick() {
        val validationResult = validateRegistrationInput.execute(uiState.value.registerUsername, uiState.value.registerEmail, uiState.value.registerPassword, uiState.value.registerConfirmPassword)
        if (!validationResult.successful) {
            _uiState.update { it.copy(snackbarMessage = validationResult.errorMessage) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            FirebaseManager.auth.createUserWithEmailAndPassword(uiState.value.registerEmail, uiState.value.registerPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update { it.copy(isLoading = false, registrationSuccess = true, snackbarMessage = "Usuario registrado con éxito") }
                    } else {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = task.exception?.message ?: "Error de registro") }
                    }
                }
        }
    }

    fun onLoginClick() {
        val validationResult = validateLoginInput.execute(uiState.value.loginEmailOrUsername, uiState.value.loginPassword)
        if (!validationResult.successful) {
            _uiState.update { it.copy(snackbarMessage = validationResult.errorMessage) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            FirebaseManager.auth.signInWithEmailAndPassword(uiState.value.loginEmailOrUsername, uiState.value.loginPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = task.exception?.message ?: "Error de inicio de sesión") }
                    }
                }
        }
    }

    fun onGuardarDireccionClick() {
        val currentUser = FirebaseManager.auth.currentUser
        if (currentUser == null) {
            _uiState.update { it.copy(snackbarMessage = "Debes iniciar sesión para guardar una dirección.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val direccionData = mapOf(
                "direccion" to uiState.value.direccion,
                "ciudad" to uiState.value.ciudad,
                "region" to uiState.value.region,
                "codigoPostal" to uiState.value.codigoPostal
            )
            FirebaseManager.firestore
                .collection("usuarios")
                .document(currentUser.uid)
                .set(direccionData)
                .addOnSuccessListener {
                    _uiState.update { it.copy(isLoading = false, direccionGuardadaConExito = true, snackbarMessage = "Dirección guardada con éxito") }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = "Error al guardar la dirección: ${e.message}") }
                }
        }
    }

    // --- HANDLERS DE UI (ACTUALIZADORES DE ESTADO) ---
    fun onRegisterUsernameChanged(username: String) { _uiState.update { it.copy(registerUsername = username) } }
    fun onRegisterEmailChanged(email: String) { _uiState.update { it.copy(registerEmail = email) } }
    fun onRegisterPasswordChanged(password: String) { _uiState.update { it.copy(registerPassword = password) } }
    fun onRegisterConfirmPasswordChanged(confirmPassword: String) { _uiState.update { it.copy(registerConfirmPassword = confirmPassword) } }
    fun registrationCompleted() { _uiState.update { it.copy(registrationSuccess = false) } }
    fun onLoginEmailOrUsernameChanged(value: String) { _uiState.update { it.copy(loginEmailOrUsername = value) } }
    fun onLoginPasswordChanged(password: String) { _uiState.update { it.copy(loginPassword = password) } }
    fun loginCompleted() { _uiState.update { it.copy(loginSuccess = false) } }
    fun onFotoPerfilCambiada(uri: Uri?) { _uiState.update { it.copy(fotoPerfilUri = uri) } }
    fun snackbarMostrado() { _uiState.update { it.copy(snackbarMessage = null) } }
    fun onDireccionChanged(value: String) { _uiState.update { it.copy(direccion = value) } }
    fun onCiudadChanged(value: String) { _uiState.update { it.copy(ciudad = value) } }
    fun onRegionChanged(value: String) { _uiState.update { it.copy(region = value) } }
    fun onCodigoPostalChanged(value: String) { _uiState.update { it.copy(codigoPostal = value) } }
    fun direccionGuardada() { _uiState.update { it.copy(direccionGuardadaConExito = false) } }
}
