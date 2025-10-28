package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.data.Producto

@OptIn(ExperimentalMaterial3Api::class) // Necesario para stickyHeader
@Composable
fun ProductScreen(
    mainViewModel: MainViewModel = viewModel(),
    // onNavigate es necesario si esta pantalla navega a otra
    onNavigate: (String) -> Unit
) {

    val uiState by mainViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
    ) {

        item {
            Text(
                text = "Catálogo Completo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // --- SECCIÓN VERDURAS ---
        stickyHeader { // El título "Verduras" se quedará pegado arriba
            Header(text = "Verduras")
        }

        items(uiState.verduras) { producto ->
            CatalogoItemCard(producto = producto)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- SECCIÓN FRUTAS ---
        stickyHeader { // El título "Frutas" se quedará pegado arriba
            Header(text = "Frutas")
        }

        items(uiState.frutas) { producto ->
            CatalogoItemCard(producto = producto)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/**
 * Tarjeta simple para el catálogo (coincide con image_a9ed87.png)
 */
@Composable
fun CatalogoItemCard(producto: Producto) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = producto.imagenRes),
            contentDescription = producto.nombre,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${"%.0f".format(producto.precio)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

/**
 * Composable simple para los títulos de sección (Verduras, Frutas)
 */
@Composable
fun Header(text: String) {
    Surface( // Fondo blanco para tapar el contenido que pasa por debajo
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}