package com.example.huertohogartiendaapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.data.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

// --- ESTADO PARA LOS PRODUCTOS ---
data class MainUiState(
    val productosMuestra: List<Producto> = emptyList(),
    val verduras: List<Producto> = emptyList(),
    val frutas: List<Producto> = emptyList()
)

// --- ESTADO PARA EL REGISTRO ---
data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // Mensajes de error (¡ESTAS LÍNEAS SON LAS CLAVE!)
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    // Estado de la operación
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false
)

// --- ESTADO PARA EL LOGIN ---
data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",

    // Mensajes de error (¡ESTAS LÍNEAS SON LAS CLAVE!)
    val emailOrUsernameError: String? = null,
    val passwordError: String? = null,

    // Estado de la operación
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)


class MainViewModel : ViewModel() {

    // --- LÓGICA DE PRODUCTOS ---
    private val repository = ProductoRepository
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        val muestra = repository.getProductosDeMuestra()
        val verduras = repository.getVerduras()
        val frutas = repository.getFrutas()
        _uiState.update { estadoActual ->
            estadoActual.copy(
                productosMuestra = muestra,
                verduras = verduras,
                frutas = frutas
            )
        }
    }

    // --- LÓGICA DE REGISTRO ---

    // Expresión regular simple para validar email
    private val EMAIL_VALIDATION_REGEX = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    // Funciones que la UI llamará cuando el texto cambie
    fun onRegisterUsernameChanged(username: String) {
        _registerUiState.update { it.copy(username = username, usernameError = null) }
    }
    fun onRegisterEmailChanged(email: String) {
        _registerUiState.update { it.copy(email = email, emailError = null) }
    }
    fun onRegisterPasswordChanged(password: String) {
        _registerUiState.update { it.copy(password = password, passwordError = null) }
    }
    fun onRegisterConfirmPasswordChanged(confirmPass: String) {
        _registerUiState.update { it.copy(confirmPassword = confirmPass, confirmPasswordError = null) }
    }

    // Función que la UI llamará al pulsar el botón "Registrar"
    fun onRegisterClick() {
        _registerUiState.update { it.copy(isLoading = true) }

        // Validar campos
        val hasError = validateRegistrationFields()

        if (!hasError) {
            // ¡Validación exitosa!
            // TODO: Aquí es donde llamarías a Firebase, etc.
            // Por ahora, simulamos un éxito:
            _registerUiState.update {
                it.copy(isLoading = false, registrationSuccess = true)
            }
        } else {
            // La validación falló, los mensajes de error ya se mostraron
            _registerUiState.update { it.copy(isLoading = false) }
        }
    }

    private fun validateRegistrationFields(): Boolean {
        val state = _registerUiState.value
        var hasError = false

        // 1. Validar Nombre de Usuario
        if (state.username.isBlank()) {
            _registerUiState.update { it.copy(usernameError = "El nombre no puede estar vacío") }
            hasError = true
        }

        // 2. Validar Email
        if (state.email.isBlank()) {
            _registerUiState.update { it.copy(emailError = "El email no puede estar vacío") }
            hasError = true
        } else if (!EMAIL_VALIDATION_REGEX.matcher(state.email).matches()) {
            _registerUiState.update { it.copy(emailError = "El formato del email no es válido") }
            hasError = true
        }

        // 3. Validar Contraseña
        if (state.password.isBlank()) {
            _registerUiState.update { it.copy(passwordError = "La contraseña no puede estar vacía") }
            hasError = true
        } else if (state.password.length < 6) {
            _registerUiState.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }
            hasError = true
        }

        // 4. Validar Confirmación de Contraseña
        if (state.confirmPassword != state.password) {
            _registerUiState.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden") }
            hasError = true
        }

        return hasError
    }


    // --- LÓGICA DE LOGIN ---
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // Funciones que la UI llamará cuando el texto de login cambie
    fun onLoginEmailOrUsernameChanged(username: String) {
        _loginUiState.update { it.copy(emailOrUsername = username, emailOrUsernameError = null) }
    }
    fun onLoginPasswordChanged(password: String) {
        _loginUiState.update { it.copy(password = password, passwordError = null) }
    }

    // Función que la UI llamará al pulsar el botón "Iniciar Sesión"
    fun onLoginClick() {
        _loginUiState.update { it.copy(isLoading = true) }

        // Validar campos
        val hasError = validateLoginFields()

        if (!hasError) {
            // ¡Validación exitosa!
            // TODO: Aquí es donde llamarías a Firebase, etc.
            // Por ahora, simulamos un éxito:
            _loginUiState.update {
                it.copy(isLoading = false, loginSuccess = true)
            }
        } else {
            // La validación falló
            _loginUiState.update { it.copy(isLoading = false) }
        }
    }

    private fun validateLoginFields(): Boolean {
        val state = _loginUiState.value
        var hasError = false

        // 1. Validar Usuario/Email
        if (state.emailOrUsername.isBlank()) {
            _loginUiState.update { it.copy(emailOrUsernameError = "El campo no puede estar vacío") }
            hasError = true
        }

        // 2. Validar Contraseña
        if (state.password.isBlank()) {
            _loginUiState.update { it.copy(passwordError = "La contraseña no puede estar vacía") }
            hasError = true
        }

        return hasError
    }
}