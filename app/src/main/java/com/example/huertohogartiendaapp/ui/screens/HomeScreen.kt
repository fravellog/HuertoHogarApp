package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.huertohogartiendaapp.MainViewModel
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.util.formatToCLP

@Composable
fun HomeScreen(mainViewModel: MainViewModel = viewModel()) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Llama a cargar los productos desde Firebase cuando la pantalla aparece
    LaunchedEffect(Unit) {
        mainViewModel.cargarProductos()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.placeholder_banner), // Banner
                contentDescription = "Banner Huerto Hogar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Text(
                text = "Ofertas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Muestra un indicador de carga o los productos
        if (uiState.isLoadingProductos && (uiState.verduras.isEmpty() && uiState.frutas.isEmpty())) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            val productosEnOferta = (uiState.verduras + uiState.frutas).shuffled().take(3)
            if (productosEnOferta.isEmpty()) {
                item { Text("No hay ofertas disponibles en este momento.") }
            } else {
                items(productosEnOferta, key = { it.id }) { producto ->
                    OfferItem(
                        producto = producto,
                        onAddToCartClicked = { mainViewModel.agregarAlCarrito(producto) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OfferItem(producto: Producto, onAddToCartClicked: () -> Unit) {
    // Animación de entrada para la tarjeta de oferta
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = 100)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(durationMillis = 500, delayMillis = 100)
                ),
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(100.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(2.dp, Color(0xFFA5D6A7))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Carga la imagen desde la URL de Firebase
                    AsyncImage(
                        model = producto.imagen,
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.placeholder_banner),
                        error = painterResource(id = R.drawable.placeholder_banner)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                        Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = formatToCLP(producto.precio), fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddToCartClicked,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0D6F5))
            ) {
                Text(text = "Agregar al Carrito", color = Color.DarkGray.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}
