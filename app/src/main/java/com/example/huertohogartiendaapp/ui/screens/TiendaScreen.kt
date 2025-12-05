package com.example.huertohogartiendaapp.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.util.formatToCLP
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TiendaScreen(mainViewModel: MainViewModel = viewModel()) {
    val uiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.cargarProductos()
    }

    val verduras = uiState.verduras
    val frutas = uiState.frutas

    if (uiState.isLoadingProductos && (verduras.isEmpty() && frutas.isEmpty())) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Verduras Category
            if (verduras.isNotEmpty()) {
                stickyHeader {
                    CategoryHeader(title = "Verduras")
                }

                items(verduras.chunked(2)) { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { producto ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    producto = producto,
                                    onAddToCartClicked = { mainViewModel.agregarAlCarrito(producto) }
                                )
                            }
                        }
                        // Add a spacer for alignment if the row is not full
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Frutas Category
            if (frutas.isNotEmpty()) {
                stickyHeader {
                    CategoryHeader(title = "Frutas")
                }

                items(frutas.chunked(2)) { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { producto ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    producto = producto,
                                    onAddToCartClicked = { mainViewModel.agregarAlCarrito(producto) }
                                )
                            }
                        }
                        // Add a spacer for alignment if the row is not full
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(title: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), color = MaterialTheme.colorScheme.background) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
    val context = LocalContext.current
    val imageName = producto.imagen.trim().lowercase()
    val imageResId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    // LOG DE DIAGNÓSTICO
    Log.d("ImageLoading", "TiendaScreen - Producto: ${producto.nombre}, Buscando drawable: '$imageName', ID Encontrado: $imageResId")


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.placeholder_banner),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
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
