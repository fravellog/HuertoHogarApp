package com.example.huertohogartiendaapp

import com.example.huertohogartiendaapp.util.Validation
import org.junit.Assert.*
import org.junit.Test

class ValidateRegistrationInputTest {

    // --- PRUEBAS PARA LA VALIDACIÓN DE EMAIL ---

    @Test
    fun `email es válido`() {
        assertTrue(Validation.isValidEmail("test@example.com"))
    }

    @Test
    fun `email sin @ no es válido`() {
        assertFalse(Validation.isValidEmail("testexample.com"))
    }

    @Test
    fun `email sin dominio no es válido`() {
        assertFalse(Validation.isValidEmail("test@"))
    }

    @Test
    fun `email vacío no es válido`() {
        assertFalse(Validation.isValidEmail(""))
    }

    // --- PRUEBAS PARA LA VALIDACIÓN DE CONTRASEÑA ---

    @Test
    fun `contraseña es válida`() {
        assertTrue(Validation.isValidPassword("123456"))
    }

    @Test
    fun `contraseña corta no es válida`() {
        assertFalse(Validation.isValidPassword("123"))
    }

    @Test
    fun `contraseña vacía no es válida`() {
        assertFalse(Validation.isValidPassword(""))
    }

    // --- PRUEBAS PARA LA COINCIDENCIA DE CONTRASEÑAS ---

    @Test
    fun `contraseñas coinciden`() {
        assertTrue(Validation.doPasswordsMatch("password123", "password123"))
    }

    @Test
    fun `contraseñas no coinciden`() {
        assertFalse(Validation.doPasswordsMatch("password123", "password456"))
    }

    @Test
    fun `una contraseña vacía no coincide`() {
        assertFalse(Validation.doPasswordsMatch("password123", ""))
    }
}
