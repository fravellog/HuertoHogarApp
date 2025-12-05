package com.example.huertohogartiendaapp.util

import android.util.Patterns

object Validation {
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Ejemplo: requiere al menos 6 caracteres
        return password.length >= 6
    }

    fun doPasswordsMatch(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }
}
