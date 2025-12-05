package com.example.huertohogartiendaapp.ui.screens

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogartiendaapp.data.CarritoItem
import com.example.huertohogartiendaapp.data.FirebaseManager
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.data.ProductoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


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
    val direccionGuardadaConExito: Boolean = false,
    val registerUsernameError: String? = null,
    val registerEmailError: String? = null,
    val registerPasswordError: String? = null,
    val registerConfirmPasswordError: String? = null,
    val errorMessage: String? = null,
    val loginEmailError: String? = null,
    val loginPasswordError: String? = null,
    val isAdmin: Boolean = false,

    // Propiedades para el formulario de producto
    val productNombre: String = "",
    val productPrecio: String = "",
    val productStock: String = "",
    val productCategoria: String = "Verduras",
    val productImagen: String = "",
    val productSaveSuccess: Boolean = false
) {
    val totalCarrito: Double
        get() = productosEnCarrito.sumOf { it.producto.precio.toDouble() * it.cantidad }
}

// --- CLASE PRINCIPAL DEL VIEWMODEL ---

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    private val productoRepository = ProductoRepository()
    private val authStateListener: FirebaseAuth.AuthStateListener

    init {
        cargarProductos()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                Log.d("AdminCheck", "Auth state changed: User is signed in with UID: ${firebaseUser.uid}")
                checkUserAdminStatus()
            } else {
                Log.d("AdminCheck", "Auth state changed: User is signed out.")
                _uiState.update { it.copy(isAdmin = false) }
            }
        }
        FirebaseManager.auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseManager.auth.removeAuthStateListener(authStateListener)
    }

    fun cargarProductos() {
        productoRepository.getTodosLosProductosEnTiempoReal()
            .onEach { productosDesdeFirebase ->
                _uiState.update {
                    it.copy(
                        verduras = productosDesdeFirebase.filter { p -> p.categoria == "Verduras" },
                        frutas = productosDesdeFirebase.filter { p -> p.categoria == "Frutas" },
                        isLoadingProductos = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun getProductById(productId: String): Producto? {
        val allProducts = _uiState.value.verduras + _uiState.value.frutas
        return allProducts.find { it.id == productId }
    }

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

    fun onRegisterClick() {
        var hasError = false
        _uiState.update {
            it.copy(
                registerUsernameError = null,
                registerEmailError = null,
                registerPasswordError = null,
                registerConfirmPasswordError = null,
                errorMessage = null
            )
        }

        if (uiState.value.registerUsername.isBlank()) {
            _uiState.update { it.copy(registerUsernameError = "El nombre de usuario no puede estar vacío") }
            hasError = true
        }

        if (uiState.value.registerEmail.isBlank()) {
            _uiState.update { it.copy(registerEmailError = "El email no puede estar vacío") }
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(uiState.value.registerEmail).matches()) {
            _uiState.update { it.copy(registerEmailError = "El formato del email no es válido") }
            hasError = true
        }

        if (uiState.value.registerPassword.isBlank()) {
            _uiState.update { it.copy(registerPasswordError = "La contraseña no puede estar vacía") }
            hasError = true
        }

        if (uiState.value.registerConfirmPassword.isBlank()) {
            _uiState.update { it.copy(registerConfirmPasswordError = "La confirmación de la contraseña no puede estar vacía") }
            hasError = true
        }

        if (uiState.value.registerPassword != uiState.value.registerConfirmPassword) {
            _uiState.update { it.copy(registerPasswordError = "Las contraseñas no coinciden", registerConfirmPasswordError = "Las contraseñas no coinciden") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            FirebaseManager.auth.createUserWithEmailAndPassword(uiState.value.registerEmail, uiState.value.registerPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        if (user != null) {
                            val email = uiState.value.registerEmail
                            val isAdmin = email.endsWith("@admin.com")
                            val userData = hashMapOf(
                                "username" to uiState.value.registerUsername,
                                "email" to email,
                                "isAdmin" to isAdmin
                            )
                            FirebaseManager.firestore.collection("users").document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true, snackbarMessage = "Usuario registrado con éxito") }
                                }
                                .addOnFailureListener { e ->
                                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error al guardar datos de usuario: ${e.message}") }
                                }
                        }
                    } else {
                        val exception = task.exception as? FirebaseAuthException
                        val errorMessage = when (exception?.errorCode) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> "El correo electrónico ya está en uso."
                            "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 6 caracteres."
                            else -> exception?.message ?: "Error de registro desconocido."
                        }
                        _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
                    }
                }
        }
    }

    fun onLoginClick() {
        var hasError = false
        _uiState.update { it.copy(loginEmailError = null, loginPasswordError = null, errorMessage = null) }

        if (uiState.value.loginEmailOrUsername.isBlank()) {
            _uiState.update { it.copy(loginEmailError = "El email o usuario no puede estar vacío") }
            hasError = true
        }

        if (uiState.value.loginPassword.isBlank()) {
            _uiState.update { it.copy(loginPasswordError = "La contraseña no puede estar vacía") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            FirebaseManager.auth.signInWithEmailAndPassword(uiState.value.loginEmailOrUsername, uiState.value.loginPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseManager.auth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                            if (reloadTask.isSuccessful) {
                                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                            } else {
                                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al verificar usuario.") }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Credenciales incorrectas") }
                    }
                }
        }
    }

    private fun checkUserAdminStatus() {
        val firebaseUser = FirebaseManager.auth.currentUser
        if (firebaseUser != null) {
            Log.d("AdminCheck", "Checking admin status for UID: ${firebaseUser.uid}")
            FirebaseManager.firestore.collection("users").document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Log.d("AdminCheck", "Document found: ${document.data}")
                        val isAdmin = document.getBoolean("isAdmin")
                            ?: document.getBoolean("isadmin")
                            ?: document.getBoolean("idadmin")
                            ?: false
                        Log.d("AdminCheck", "Final isAdmin value: $isAdmin")
                        _uiState.update { it.copy(isAdmin = isAdmin) }
                    } else {
                        Log.d("AdminCheck", "User document not found.")
                        _uiState.update { it.copy(isAdmin = false) }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AdminCheck", "Error checking admin status", e)
                    _uiState.update { it.copy(isAdmin = false) }
                }
        } else {
            Log.d("AdminCheck", "Cannot check admin status, no user is logged in.")
            _uiState.update { it.copy(isAdmin = false) }
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
                .collection("users")
                .document(currentUser.uid)
                .set(direccionData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    _uiState.update { it.copy(isLoading = false, direccionGuardadaConExito = true, snackbarMessage = "Dirección guardada con éxito") }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = "Error al guardar la dirección: ${e.message}") }
                }
        }
    }

    // --- HANDLERS PARA EL FORMULARIO DE PRODUCTO ---
    fun onProductNombreChanged(nombre: String) { _uiState.update { it.copy(productNombre = nombre) } }
    fun onProductPrecioChanged(precio: String) { _uiState.update { it.copy(productPrecio = precio) } }
    fun onProductStockChanged(stock: String) { _uiState.update { it.copy(productStock = stock) } }
    fun onProductCategoriaChanged(categoria: String) { _uiState.update { it.copy(productCategoria = categoria) } }
    fun onProductImagenChanged(imagen: String) { _uiState.update { it.copy(productImagen = imagen) } }
    fun productSaveCompleted() { _uiState.update { it.copy(productSaveSuccess = false) } }

    fun saveProduct() {
        // Validación básica (se puede mejorar)
        if (uiState.value.productNombre.isBlank() || uiState.value.productPrecio.isBlank() || uiState.value.productStock.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Todos los campos son obligatorios") }
            return
        }

        val producto = hashMapOf(
            "nombre" to uiState.value.productNombre,
            "precio" to (uiState.value.productPrecio.toDoubleOrNull() ?: 0.0),
            "stock" to (uiState.value.productStock.toLongOrNull() ?: 0L),
            "categoria" to uiState.value.productCategoria,
            "imagen" to uiState.value.productImagen
        )

        val collection = if (uiState.value.productCategoria == "Verduras") "verduras" else "frutas"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            FirebaseManager.firestore.collection(collection).add(producto)
                .addOnSuccessListener {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productSaveSuccess = true,
                            snackbarMessage = "Producto '${producto["nombre"]}' guardado con éxito",
                            // Limpiar formulario
                            productNombre = "",
                            productPrecio = "",
                            productStock = "",
                            productCategoria = "Verduras",
                            productImagen = ""
                        )
                    }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isLoading = false, snackbarMessage = "Error al guardar el producto: ${e.message}") }
                }
        }
    }

    // --- HANDLERS DE UI (ACTUALIZADORES DE ESTADO) ---
    fun onRegisterUsernameChanged(username: String) { _uiState.update { it.copy(registerUsername = username, registerUsernameError = null) } }
    fun onRegisterEmailChanged(email: String) { _uiState.update { it.copy(registerEmail = email, registerEmailError = null) } }
    fun onRegisterPasswordChanged(password: String) { _uiState.update { it.copy(registerPassword = password, registerPasswordError = null) } }
    fun onRegisterConfirmPasswordChanged(confirmPassword: String) { _uiState.update { it.copy(registerConfirmPassword = confirmPassword, registerConfirmPasswordError = null) } }
    fun registrationCompleted() { _uiState.update { it.copy(registrationSuccess = false) } }
    fun onLoginEmailOrUsernameChanged(value: String) { _uiState.update { it.copy(loginEmailOrUsername = value, loginEmailError = null) } }
    fun onLoginPasswordChanged(password: String) { _uiState.update { it.copy(loginPassword = password, loginPasswordError = null) } }
    fun loginCompleted() { _uiState.update { it.copy(loginSuccess = false) } }
    fun onFotoPerfilCambiada(uri: Uri?) { _uiState.update { it.copy(fotoPerfilUri = uri) } }
    fun snackbarMostrado() { _uiState.update { it.copy(snackbarMessage = null) } }
    fun onDireccionChanged(value: String) { _uiState.update { it.copy(direccion = value) } }
    fun onCiudadChanged(value: String) { _uiState.update { it.copy(ciudad = value) } }
    fun onRegionChanged(value: String) { _uiState.update { it.copy(region = value) } }
    fun onCodigoPostalChanged(value: String) { _uiState.update { it.copy(codigoPostal = value) } }
    fun direccionGuardada() { _uiState.update { it.copy(direccionGuardadaConExito = false) } }
}
