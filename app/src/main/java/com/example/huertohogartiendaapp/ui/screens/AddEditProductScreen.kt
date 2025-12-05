package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Efecto para navegar hacia atrás cuando el producto se guarda con éxito
    LaunchedEffect(uiState.productSaveSuccess) {
        if (uiState.productSaveSuccess) {
            mainViewModel.productSaveCompleted() // Resetea el estado
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mainViewModel.saveProduct() }, // Llama a la función del ViewModel
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Done, contentDescription = "Guardar Producto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.productNombre,
                onValueChange = { mainViewModel.onProductNombreChanged(it) },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.productPrecio,
                onValueChange = { mainViewModel.onProductPrecioChanged(it) },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.productStock,
                onValueChange = { mainViewModel.onProductStockChanged(it) },
                label = { Text("Stock Disponible") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.productImagen,
                onValueChange = { mainViewModel.onProductImagenChanged(it) },
                label = { Text("Nombre de la imagen (ej: tomate.jpg)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Categoría
            Text("Categoría")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = uiState.productCategoria == "Verduras",
                    onClick = { mainViewModel.onProductCategoriaChanged("Verduras") }
                )
                Text("Verduras", modifier = Modifier.padding(start = 4.dp, end = 16.dp))
                RadioButton(
                    selected = uiState.productCategoria == "Frutas",
                    onClick = { mainViewModel.onProductCategoriaChanged("Frutas") }
                )
                Text("Frutas", modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
