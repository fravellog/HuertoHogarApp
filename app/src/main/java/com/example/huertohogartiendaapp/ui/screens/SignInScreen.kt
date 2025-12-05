package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignInScreen(
    mainViewModel: MainViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
            mainViewModel.loginCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // --- CAMPO EMAIL/USUARIO CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.loginEmailOrUsername,
            onValueChange = mainViewModel::onLoginEmailOrUsernameChanged,
            label = { Text("Email o Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.loginEmailError != null,
            supportingText = { uiState.loginEmailError?.let { Text(it) } }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- CAMPO CONTRASEÑA CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.loginPassword,
            onValueChange = mainViewModel::onLoginPasswordChanged,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = uiState.loginPasswordError != null,
            supportingText = { uiState.loginPasswordError?.let { Text(it) } }
        )

        // Mensaje de error general para credenciales incorrectas
        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = mainViewModel::onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("INICIAR SESIÓN")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "¿No tienes una cuenta? Crea una",
            modifier = Modifier.clickable(onClick = onCreateAccountClick)
        )
    }
}
