package com.example.huertohogartiendaapp.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importante para obtener el ViewModel en un Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme


// Esta es la función "inteligente" que llama al ViewModel
@Composable
fun ProductScreen(
    // Así se obtiene la instancia del ViewModel
    viewModel: MainViewModel = viewModel()
) {
    // Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()


    // Llamamos a la pantalla "tonta" que solo muestra los datos
    PantallaPrincipal(
        productosMuestra = uiState.productosMuestra,
        productosSeccion = uiState.productosSeccion
    )
}


// --- Este es el código que ya tenías, solo que movido aquí ---
@Composable
fun PantallaPrincipal(
    productosMuestra: List<Producto>,
    productosSeccion: List<Producto>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Text(
                text = "Nuestros Productos Destacados",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(productosMuestra) { producto ->
            ProductoItem(producto = producto)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Catálogo Completo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(productosSeccion) { producto ->
            ProductoItem(producto = producto)
        }
    }
}


// --- Este código también lo tenías y se mueve aquí ---
@Composable
fun ProductoItem(producto: Producto, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(text = producto.nombre, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Text(text = "$${producto.precio}", color = MaterialTheme.colorScheme.primary)
        Text(text = producto.descripcion, fontSize = 14.sp)
    }
}


// --- Preview para esta pantalla ---
@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    HuertoHogarTiendaAppTheme {
        val muestra = listOf(
            Producto("1", "Manzana", 1200.0, 10, "Manzana roja", "Frutas")
        )
        val todos = listOf(
            Producto("1", "Manzana", 1200.0, 10, "Manzana roja", "Frutas"),
            Producto("2", "Lechuga", 900.0, 20, "Lechuga fresca", "Verduras")
        )
        // Usamos la pantalla "tonta" para la preview
        PantallaPrincipal(productosMuestra = muestra, productosSeccion = todos)
    }
}