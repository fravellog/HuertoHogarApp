package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.ui.screens.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Efecto para volver atrás cuando la dirección se guarde con éxito
    LaunchedEffect(uiState.direccionGuardadaConExito) {
        if (uiState.direccionGuardadaConExito) {
            mainViewModel.direccionGuardada() // Resetea el estado
            onNavigateBack() // Vuelve a la pantalla de Perfil
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Dirección de Envío") },
                // Puedes añadir un botón de "atrás" si lo deseas
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo Dirección
            item {
                OutlinedTextField(
                    value = uiState.direccion,
                    onValueChange = mainViewModel::onDireccionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Dirección (Calle y Número)") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Campo Ciudad
            item {
                OutlinedTextField(
                    value = uiState.ciudad,
                    onValueChange = mainViewModel::onCiudadChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ciudad") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Campo Región
            item {
                OutlinedTextField(
                    value = uiState.region,
                    onValueChange = mainViewModel::onRegionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Región") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Campo Código Postal
            item {
                OutlinedTextField(
                    value = uiState.codigoPostal,
                    onValueChange = mainViewModel::onCodigoPostalChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Código Postal (Opcional)") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            // Botón de Guardar
            item {
                Button(
                    onClick = mainViewModel::onGuardarDireccionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("GUARDAR DIRECCIÓN")
                    }
                }
            }
        }
    }
}
