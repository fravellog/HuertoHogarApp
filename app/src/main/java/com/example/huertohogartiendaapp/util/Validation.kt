package com.example.huertohogartiendaapp.util

import android.util.Patterns

/**
 * Clase dedicada a validar los campos de entrada del formulario de registro.
 * Contiene un resultado ('ValidationResult') para saber si fue exitoso o tuvo un error.
 */
class ValidateRegistrationInput {

    fun execute(
        username: String,
        email: String,
        pass: String,
        repeatedPass: String
    ): ValidationResult {
        // --- Validación del nombre de usuario ---
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre de usuario no puede estar vacío."
            )
        }

        // --- Validación del correo electrónico ---
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo no puede estar vacío."
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato del correo no es válido."
            )
        }

        // --- Validación de la contraseña ---
        if (pass.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 caracteres."
            )
        }
        val containsLettersAndDigits = pass.any { it.isDigit() } && pass.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener letras y números."
            )
        }

        // --- Validación de la confirmación de contraseña ---
        if (pass != repeatedPass) {
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas no coinciden."
            )
        }

        // Si todas las validaciones pasan
        return ValidationResult(successful = true)
    }
}
class ValidateLoginInput {

    fun execute(emailOrUsername: String, pass: String): ValidationResult {
        // --- Validación del campo de usuario/correo ---
        if (emailOrUsername.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El campo de usuario o correo no puede estar vacío."
            )
        }

        // --- Validación de la contraseña ---
        if (pass.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede estar vacía."
            )
        }

        // Si todas las validaciones pasan
        return ValidationResult(successful = true)
    }
}
/**
 * Un data class simple para encapsular el resultado de una validación.
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
