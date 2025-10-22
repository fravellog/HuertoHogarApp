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
import androidx.compose.material3.Icon
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
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme


@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit
) {
    // Estados para los 4 campos
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo
        Image(
            painter = painterResource(id = R.drawable.logo_huerta),
            contentDescription = "Logo Chile Huerta",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
        )


        // 2. Título "Registro"
        Text(
            text = "Registro",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // 3. Campo de Usuario
        Text(
            text = "Nombre de Usuario",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingrese un nombre de usuario") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(16.dp))


        // 4. Campo de Correo
        Text(
            text = "Correo electrónico",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Ingrese un correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )


        Spacer(modifier = Modifier.height(16.dp))


        // 5. Campo de Contraseña
        Text(
            text = "Contraseña",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingrese una contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )


        Spacer(modifier = Modifier.height(16.dp))


        // 6. Campo de Repetir Contraseña
        Text(
            text = "Repita su contraseña",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Escriba nuevamente su contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )


        Spacer(modifier = Modifier.height(32.dp))


        // 7. Botón "Registrar cuenta"
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0D6F5) // Color lila
            )
        ) {
            Text(text = "Registrar cuenta", color = Color.DarkGray.copy(alpha = 0.8f))
        }
    }
}


// Vista previa
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun RegisterScreenPreview() {
    HuertoHogarTiendaAppTheme {
        RegisterScreen(onRegisterClick = {})
    }
}