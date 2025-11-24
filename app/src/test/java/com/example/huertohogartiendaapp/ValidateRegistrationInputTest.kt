package com.example.huertohogartiendaapp.util

import android.util.Patterns

class ValidateRegistrationInput {

    fun execute(
        username: String,        email: String,
        pass: String,
        repeatedPass: String
    ): ValidationResult {
        // 1. Validar que el nombre de usuario no esté vacío
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre de usuario no puede estar vacío."
            )
        }

        // 2. Validar que el correo no esté vacío
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo electrónico no puede estar vacío."
            )
        }

        // 3. Validar que el formato del correo sea válido
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato del correo electrónico no es válido."
            )
        }

        // 4. Validar que la contraseña no esté vacía
        if (pass.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede estar vacía."
            )
        }

        // 5. Validar que la contraseña tenga un largo mínimo
        if (pass.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 caracteres."
            )
        }

        // 6. Validar que las contraseñas coincidan
        if (pass != repeatedPass) {
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas no coinciden."
            )
        }

        // 7. Si todas las validaciones pasan, el resultado es exitoso
        return ValidationResult(
            successful = true
        )
    }
}
