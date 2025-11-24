package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.MainViewModel
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme

@Composable
fun SignInScreen(
    mainViewModel: MainViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    // Observamos el estado principal de la UI
    val uiState by mainViewModel.uiState.collectAsState()

    // Efecto para navegar cuando el login sea exitoso
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
            // Reseteamos el estado para evitar bucles de navegación
            mainViewModel.loginCompleted()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo_huerta),
                contentDescription = "Logo Huerto Hogar",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "Iniciar Sesión",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        item {
            // Campo de Usuario/Email
            OutlinedTextField(
                value = uiState.loginEmailOrUsername,
                onValueChange = mainViewModel::onLoginEmailOrUsernameChanged,
                label = { Text("Usuario o correo electrónico") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
                // Ya no se necesita 'isError' ni 'supportingText'
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Campo de Contraseña
            OutlinedTextField(
                value = uiState.loginPassword,
                onValueChange = mainViewModel::onLoginPasswordChanged,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                // Ya no se necesita 'isError' ni 'supportingText'
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // Botón "Inicio de sesión"
            Button(
                onClick = mainViewModel::onLoginClick,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0D6F5)),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Inicio de sesión", color = Color.DarkGray.copy(alpha = 0.8f))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Botón "Crear una cuenta"
            Text(text = "¿No tienes una cuenta?")
            TextButton(onClick = onCreateAccountClick, enabled = !uiState.isLoading) {
                Text(
                    text = "Crear una cuenta",
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFFE0D6F5), shape = ButtonDefaults.shape)
                        .padding(vertical = 8.dp, horizontal = 24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SignInScreenPreview() {
    HuertoHogarTiendaAppTheme {
        SignInScreen(onLoginSuccess = {}, onCreateAccountClick = {})
    }
}
