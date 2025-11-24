package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.MainViewModel

@Composable
fun RegisterScreen(
    mainViewModel: MainViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    // Este LaunchedEffect se activa cuando el registro es exitoso
    // para navegar a la pantalla anterior.
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            // Llama a la función de éxito y resetea el estado en el ViewModel.
            onRegistrationSuccess()
            mainViewModel.registrationCompleted()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- TÍTULO ---
        item {
            Text(
                text = "Crear una Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // --- CAMPO USERNAME ---
        item {
            OutlinedTextField(
                value = uiState.registerUsername,
                onValueChange = mainViewModel::onRegisterUsernameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre de usuario") },
                // Ya no necesitamos la lógica de 'isError' ni el Text de error aquí
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- CAMPO EMAIL ---
        item {
            OutlinedTextField(
                value = uiState.registerEmail,
                onValueChange = mainViewModel::onRegisterEmailChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- CAMPO CONTRASEÑA ---
        item {
            OutlinedTextField(
                value = uiState.registerPassword,
                onValueChange = mainViewModel::onRegisterPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- CAMPO CONFIRMAR CONTRASEÑA ---
        item {
            OutlinedTextField(
                value = uiState.registerConfirmPassword,
                onValueChange = mainViewModel::onRegisterConfirmPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- BOTÓN DE REGISTRO ---
        item {
            Button(
                onClick = mainViewModel::onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("REGISTRARSE")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- TEXTO PARA IR A LOGIN ---
        item {
            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes una cuenta? Inicia sesión")
            }
        }
    }
}
