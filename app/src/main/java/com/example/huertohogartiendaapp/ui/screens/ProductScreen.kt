package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage // <-- Import para imágenes desde URL
import com.example.huertohogartiendaapp.MainViewModel
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme
import com.example.huertohogartiendaapp.util.formatToCLP

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Carga los productos desde Firebase cuando la pantalla aparece
    LaunchedEffect(Unit) {
        mainViewModel.cargarProductos()
    }

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

        if (uiState.isLoadingProductos && (uiState.verduras.isEmpty() && uiState.frutas.isEmpty())) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // --- SECCIÓN VERDURAS ---
            if (uiState.verduras.isNotEmpty()) {
                stickyHeader {
                    Header(text = "Verduras")
                }
                items(uiState.verduras, key = { it.id }) { producto ->
                    CatalogoItemCard(producto = producto)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // --- SECCIÓN FRUTAS ---
            if (uiState.frutas.isNotEmpty()) {
                stickyHeader {
                    Header(text = "Frutas")
                }
                items(uiState.frutas, key = { it.id }) { producto ->
                    CatalogoItemCard(producto = producto)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CatalogoItemCard(producto: Producto) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- CORRECCIÓN AQUÍ ---
        AsyncImage(
            model = producto.imagen, // Se usa la propiedad correcta
            contentDescription = producto.nombre,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.placeholder_banner),
            error = painterResource(id = R.drawable.placeholder_banner)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatToCLP(producto.precio),
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

@Composable
fun Header(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
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

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    HuertoHogarTiendaAppTheme {
        ProductScreen(onNavigate = {})
    }
}
