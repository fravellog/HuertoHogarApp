package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.data.Producto

// ...otras importaciones
import androidx.compose.foundation.BorderStroke // <-- ¡AÑADE ESTA LÍNEA!
import androidx.compose.foundation.layout.*
// ...otras importaciones

@Composable
fun TiendaScreen(
    mainViewModel: MainViewModel = viewModel(),
    // Necesitamos esto para que el botón "Ver productos" funcione
    onNavigate: (String) -> Unit
) {
    // Obtenemos el estado de la UI desde el ViewModel
    val uiState by mainViewModel.uiState.collectAsState()

    // Usamos LazyColumn para que la lista sea scrolleable
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 100.dp) // Espacio para que no tape el botón
    ) {

        // Título "Ofertas"
        item {
            Text(
                text = "Ofertas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Creamos una tarjeta por cada producto en la lista "productosMuestra"
        items(uiState.productosMuestra) { producto ->
            ProductoOfertCard(
                producto = producto,
                onAddToCartClick = { /* TODO: Lógica para añadir al carrito */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón "Ver productos" al final
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigate("products") }, // Navega a la ruta "products"
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0D6F5) // Color lila
                )
            ) {
                Text("Ver productos", fontSize = 16.sp)
            }
        }
    }
}

/**
 * Composable para la tarjeta de producto en la sección "Ofertas".
 * Coincide con tu diseño image_526d99.png
 */
@Composable
fun ProductoOfertCard(
    producto: Producto,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F5F0)), // Verde muy pálido
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9)) // Borde verde pálido
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono, Nombre y Precio
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito",
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF4CAF50) // Verde
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Precio: $${"%.0f".format(producto.precio)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Imagen del producto
            Image(
                painter = painterResource(id = producto.imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }

    // Botón "Agregar al Carrito" (separado de la tarjeta)
    Button(
        onClick = onAddToCartClick,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE0D6F5) // Color lila
        )
    ) {
        Text("Agregar al Carrito")
    }
}