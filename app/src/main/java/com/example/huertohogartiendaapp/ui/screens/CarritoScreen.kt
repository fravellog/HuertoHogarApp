package com.example.huertohogartiendaapp.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.data.CarritoItem
import com.example.huertohogartiendaapp.util.formatToCLP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    mainViewModel: MainViewModel = viewModel(),
    navController: NavController
) {
    val uiState by mainViewModel.uiState.collectAsState()

    if (uiState.productosEnCarrito.isEmpty()) {
        EmptyCartScreen(onGoToShopClicked = {
            navController.navigate("tienda")
        })
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mi Canasta", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                CartSummary(
                    total = uiState.totalCarrito,
                    onCheckoutClicked = { /* TODO: Navegar a la pantalla de pago */ }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.productosEnCarrito, key = { it.producto.id }) { item ->
                    CarritoItemCard(
                        item = item,
                        onIncrement = { mainViewModel.incrementarCantidad(item.producto.id) },
                        onDecrement = { mainViewModel.decrementarCantidad(item.producto.id) },
                        onRemove = { mainViewModel.eliminarProducto(item.producto.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartScreen(onGoToShopClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBasket,
            contentDescription = "Carrito Vacío",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Tu canasta está vacía",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Parece que aún no has agregado nada. ¡Explora nuestros productos frescos!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onGoToShopClicked,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text("IR A LA TIENDA")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CarritoItemCard(
    item: CarritoItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val imageName = item.producto.imagen.trim().lowercase()
    val imageResId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    // LOG DE DIAGNÓSTICO
    Log.d("ImageLoading", "CarritoScreen - Producto: ${item.producto.nombre}, Buscando drawable: '$imageName', ID Encontrado: $imageResId")


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Carga la imagen desde la URL de Firebase
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.placeholder_banner),
                contentDescription = item.producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(formatToCLP(item.producto.precio), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Subtotal: ${formatToCLP(item.producto.precio * item.cantidad)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onIncrement, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, "Incrementar")
                }
                AnimatedContent(
                    targetState = item.cantidad,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInVertically { h -> h } + fadeIn() togetherWith slideOutVertically { h -> -h } + fadeOut()
                        } else {
                            slideInVertically { h -> -h } + fadeIn() togetherWith slideOutVertically { h -> h } + fadeOut()
                        }.using(SizeTransform(clip = false))
                    },
                    label = "Contador"
                ) { targetCount ->
                    Text("$targetCount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                IconButton(onClick = onDecrement, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, "Decrementar")
                }
            }
            IconButton(onClick = onRemove, modifier = Modifier.padding(start = 8.dp)) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun CartSummary(total: Double, onCheckoutClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = formatToCLP(total),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCheckoutClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("PROCEDER AL PAGO", fontWeight = FontWeight.Bold)
            }
        }
    }
}
