package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme

@Composable
fun SignInScreen(
    mainViewModel: MainViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by mainViewModel.loginUiState.collectAsState()

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_huerta),
            contentDescription = "Logo Chile Huerta",
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

        // 3. Campo de Usuario
        val emailOrUsernameError = state.emailOrUsernameError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.emailOrUsername,
            onValueChange = { mainViewModel.onLoginEmailOrUsernameChanged(it) },
            label = { Text("Usuario o correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            isError = emailOrUsernameError != null, // <-- ¡CAMBIO AQUÍ!
            supportingText = {
                if (emailOrUsernameError != null) { // <-- ¡CAMBIO AQUÍ!
                    Text(text = emailOrUsernameError, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Campo de Contraseña
        val passwordError = state.passwordError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.password,
            onValueChange = { mainViewModel.onLoginPasswordChanged(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null, // <-- ¡CAMBIO AQUÍ!
            supportingText = {
                if (passwordError != null) { // <-- ¡CAMBIO AQUÍ!
                    Text(text = passwordError, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 5. Botón "Inicio de sesión"
        Button(
            onClick = { mainViewModel.onLoginClick() },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0D6F5)
            ),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Inicio de sesión", color = Color.DarkGray.copy(alpha = 0.8f))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 6. Botón "Crear una cuenta"
        Text(text = "¿No tienes un cuenta?")
        TextButton(onClick = onCreateAccountClick, enabled = !state.isLoading) {
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

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SignInScreenPreview() {
    HuertoHogarTiendaAppTheme {
        SignInScreen(onLoginSuccess = {}, onCreateAccountClick = {})
    }
}