package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.huertohogartiendaapp.R // <-- Import correcto para R
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.util.formatToCLP
import kotlinx.coroutines.delay

@Composable
fun TiendaScreen(mainViewModel: MainViewModel = viewModel()) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Carga los productos desde Firebase cuando la pantalla aparece
    LaunchedEffect(Unit) {
        mainViewModel.cargarProductos()
    }

    // Obtenemos los productos desde el ViewModel
    val productos = uiState.verduras + uiState.frutas

    if (uiState.isLoadingProductos && productos.isEmpty()) {
        // Muestra un indicador de carga solo si la lista está vacía
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Muestra la cuadrícula de productos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos, key = { it.id }) { producto ->
                ProductCard(
                    producto = producto,
                    onAddToCartClicked = { mainViewModel.agregarAlCarrito(producto) }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ProductCard(
    producto: Producto,
    onAddToCartClicked: () -> Unit
) {
    var addedToCart by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_banner),
                error = painterResource(id = R.drawable.placeholder_banner)
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1 // Asegura que el nombre no ocupe demasiado espacio
                )
                Text(
                    text = formatToCLP(producto.precio),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Stock: ${producto.stock}",
                        fontSize = 12.sp,
                        color = if (producto.stock > 0) Color.Gray else Color.Red
                    )
                    Button(
                        onClick = {
                            onAddToCartClicked()
                            addedToCart = true
                        },
                        enabled = producto.stock > 0 && !addedToCart,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (addedToCart) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        AnimatedContent(
                            targetState = addedToCart,
                            transitionSpec = {
                                slideInVertically { h -> h } + fadeIn() togetherWith
                                        slideOutVertically { h -> -h } + fadeOut()
                            },
                            label = "BotonAnimado"
                        ) { isAdded ->
                            if (isAdded) {
                                Icon(Icons.Default.Check, "Agregado", modifier = Modifier.size(18.dp))
                            } else {
                                Text("Añadir", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Efecto para volver el botón a su estado original después de un tiempo
    if (addedToCart) {
        LaunchedEffect(Unit) {
            delay(1500)
            addedToCart = false
        }
    }
}
