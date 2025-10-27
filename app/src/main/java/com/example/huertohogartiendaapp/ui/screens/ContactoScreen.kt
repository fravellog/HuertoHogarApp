// En TiendaScreen.kt
package com.example.huertohogartiendaapp.ui.screens // Asegúrate que el package sea correcto

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ContactoScreen() {
    // Aquí irá el diseño de la tienda (lechuga, tomate, etc.)
    // Por ahora, solo un texto:
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pantalla de Tienda")
    }
}