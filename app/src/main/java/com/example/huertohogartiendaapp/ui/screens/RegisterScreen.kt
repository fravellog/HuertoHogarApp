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
fun RegisterScreen(
    mainViewModel: MainViewModel = viewModel(),
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegistrationSuccess()
            mainViewModel.registrationCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear una Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // --- CAMPO NOMBRE DE USUARIO CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.registerUsername,
            onValueChange = mainViewModel::onRegisterUsernameChanged,
            label = { Text("Nombre de Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.registerUsernameError != null,
            supportingText = { uiState.registerUsernameError?.let { Text(it) } }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- CAMPO EMAIL CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.registerEmail,
            onValueChange = mainViewModel::onRegisterEmailChanged,
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = uiState.registerEmailError != null,
            supportingText = { uiState.registerEmailError?.let { Text(it) } }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- CAMPO CONTRASEÑA CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.registerPassword,
            onValueChange = mainViewModel::onRegisterPasswordChanged,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = uiState.registerPasswordError != null,
            supportingText = { uiState.registerPasswordError?.let { Text(it) } }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- CAMPO CONFIRMAR CONTRASEÑA CON VALIDACIÓN ---
        OutlinedTextField(
            value = uiState.registerConfirmPassword,
            onValueChange = mainViewModel::onRegisterConfirmPasswordChanged,
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = uiState.registerConfirmPasswordError != null,
            supportingText = { uiState.registerConfirmPasswordError?.let { Text(it) } }
        )

        // Mensaje de error general para Firebase (opcional, por si algo más falla)
        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = mainViewModel::onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("REGISTRARSE")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "¿Ya tienes una cuenta? Inicia sesión",
            modifier = Modifier.clickable(onClick = onNavigateToLogin)
        )
    }
}
