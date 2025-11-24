package com.example.huertohogartiendaapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogartiendaapp.R

/**
 * Un banner reutilizable que muestra una imagen de fondo y un título superpuesto.
 *
 * @param titulo El texto que se mostrará en el banner.
 * @param imagenRes El ID del recurso drawable para la imagen de fondo.
 */
@Composable
fun ScreenBanner(titulo: String, @DrawableRes imagenRes: Int = R.drawable.placeholder_banner) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center // Centra el texto dentro del Box
    ) {
        // La imagen de fondo
        Image(
            painter = painterResource(id = imagenRes),
            contentDescription = "Banner de la pantalla $titulo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f // Oscurece un poco la imagen para que el texto resalte
        )

        // El texto del título
        Text(
            text = titulo,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.shadow(elevation = 8.dp) // Sombra para mejorar legibilidad
        )
    }
}
