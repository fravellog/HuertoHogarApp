package com.example.huertohogartiendaapp.ui.screens // ¡Verifica tu paquete!

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogartiendaapp.R // ¡Importa tu R!

// --- PASO 1: Define tu modelo de datos ---
// (Puedes mover esto a un archivo en tu carpeta 'data')
data class CartItem(
    val id: Int,
    val name: String,
    val pricePerUnit: Double,
    val unit: String,
    val imageResId: Int, // ID de la imagen en res/drawable
    var quantity: Int
)

// --- PASO 2: Datos de ejemplo ---
// (En un ViewModel real, esto vendría de un repositorio)
val dummyCartItems = listOf(
    CartItem(1, "Lechuga", 1180.0, "c/u", R.drawable.lechuga, 1),
    CartItem(2, "Tomate", 1610.0, "/kg", R.drawable.tomate, 2),
    CartItem(3, "Zanahoria", 1200.0, "/kg", R.drawable.zanahoria, 3)
)
// --- NOTA: Añade 'img_lechuga.png', 'img_tomate.png', 'img_zanahoria.png' a 'res/drawable' ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen() {

    // El estado de tu carrito.
    // 'by remember' asegura que la UI se actualice si la lista cambia.
    var cartItems by remember { mutableStateOf(dummyCartItems) }

    // Calcular el total
    val total = cartItems.sumOf { it.pricePerUnit * it.quantity }

    Scaffold(
        // --- BARRA SUPERIOR (TÍTULO) ---
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF0F5F0) // Un verde muy pálido
                )
            )
        },

        // --- BARRA INFERIOR (TOTAL Y PAGO) ---
        bottomBar = {
            CheckoutSummary(total = total, onCheckoutClick = { /* TODO: Lógica de pago */ })
        }
    ) { innerPadding -> // 'innerPadding' es el espacio que deja el Scaffold

        // --- CONTENIDO PRINCIPAL (LA LISTA) ---

        // Comprueba si el carrito está vacío
        if (cartItems.isEmpty()) {
            EmptyCartMessage(modifier = Modifier.padding(innerPadding))
        } else {
            // Muestra la lista de productos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Aplica el padding
                    .padding(horizontal = 16.dp), // Padding lateral
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = cartItems,
                    key = { it.id } // Ayuda a Compose a optimizar la lista
                ) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            // TODO: Actualizar esto con la lógica de tu ViewModel
                            cartItems = cartItems.map {
                                if (it.id == item.id) {
                                    it.copy(quantity = newQuantity)
                                } else {
                                    it
                                }
                            }
                        },
                        onRemoveItem = {
                            // TODO: Actualizar esto con la lógica de tu ViewModel
                            cartItems = cartItems.filter { it.id != item.id }
                        }
                    )
                }
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

/**
 * Muestra una tarjeta individual para un item en el carrito.
 */
@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre y Precio
            Column(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${item.pricePerUnit} ${item.unit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Selector de Cantidad y Eliminar
            Column(horizontalAlignment = Alignment.End) {
                QuantitySelector(
                    quantity = item.quantity,
                    onIncrease = { onQuantityChange(item.quantity + 1) },
                    onDecrease = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                )
                IconButton(onClick = onRemoveItem, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar item",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

/**
 * Muestra los botones + y - con la cantidad.
 */
@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Botón -
        IconButton(onClick = onDecrease, modifier = Modifier.size(30.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Restar uno")
        }

        Text(
            text = "$quantity",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )

        // Botón +
        IconButton(onClick = onIncrease, modifier = Modifier.size(30.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Sumar uno")
        }
    }
}

/**
 * Muestra el resumen de pago en la barra inferior.
 */
@Composable
fun CheckoutSummary(
    total: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp // Sombra para separarlo del contenido
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila del Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$${"%.0f".format(total)}", // Formatea sin decimales
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Pago
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Proceder al Pago",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Muestra un mensaje cuando el carrito está vacío.
 */
@Composable
fun EmptyCartMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito vacío",
                modifier = Modifier.size(100.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tu carrito está vacío",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray
            )
            Text(
                text = "¡Añade productos para verlos aquí!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

// --- PREVISUALIZACIÓN ---
@Preview(showBackground = true)
@Composable
fun CarritoScreenPreview() {
    CarritoScreen()
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyCartPreview() {
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }

    // Simulación de la pantalla cuando la lista está vacía
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Mi Carrito") }) },
        bottomBar = { CheckoutSummary(total = 0.0, onCheckoutClick = {}) }
    ) { innerPadding ->
        EmptyCartMessage(modifier = Modifier.padding(innerPadding))
    }
}