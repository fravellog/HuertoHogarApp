package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun RegisterScreen(
    mainViewModel: MainViewModel = viewModel(),
    onRegisterSuccess: () -> Unit
) {
    val state by mainViewModel.registerUiState.collectAsState()

    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            onRegisterSuccess()
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
            text = "Registro",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 3. Campo de Usuario
        val usernameError = state.usernameError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.username,
            onValueChange = { mainViewModel.onRegisterUsernameChanged(it) },
            label = { Text("Nombre de usuario") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            isError = usernameError != null, // <-- ¡CAMBIO AQUÍ!
            supportingText = {
                if (usernameError != null) { // <-- ¡CAMBIO AQUÍ!
                    Text(text = usernameError, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 4. Campo de Correo
        val emailError = state.emailError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.email,
            onValueChange = { mainViewModel.onRegisterEmailChanged(it) },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null, // <-- ¡CAMBIO AQUÍ!
            supportingText = {
                if (emailError != null) { // <-- ¡CAMBIO AQUÍ!
                    Text(text = emailError, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 5. Campo de Contraseña
        val passwordError = state.passwordError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.password,
            onValueChange = { mainViewModel.onRegisterPasswordChanged(it) },
            label = { Text("Contraseña (mín. 6 caracteres)") },
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
        Spacer(modifier = Modifier.height(8.dp))

        // 6. Campo de Repetir Contraseña
        val confirmPasswordError = state.confirmPasswordError // <-- ¡CAMBIO AQUÍ!
        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { mainViewModel.onRegisterConfirmPasswordChanged(it) },
            label = { Text("Repita su contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError != null, // <-- ¡CAMBIO AQUÍ!
            supportingText = {
                if (confirmPasswordError != null) { // <-- ¡CAMBIO AQUÍ!
                    Text(text = confirmPasswordError, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 7. Botón "Registrar cuenta"
        Button(
            onClick = { mainViewModel.onRegisterClick() },
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
                Text(text = "Registrar cuenta", color = Color.DarkGray.copy(alpha = 0.8f))
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun RegisterScreenPreview() {
    HuertoHogarTiendaAppTheme {
        RegisterScreen(onRegisterSuccess = {})
    }
}