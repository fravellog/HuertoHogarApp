package com.example.huertohogartiendaapp.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField // Usaremos este para el borde
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // Para el botón "Crear cuenta"
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation // Para ocultar la contraseña
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogartiendaapp.R // Importante para el logo
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme
import androidx.compose.foundation.background

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    // Estados para guardar lo que el usuario escribe
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo (reutilizado)
        Image(
            painter = painterResource(id = R.drawable.logo_huerta), // Asume que tu logo se llama así
            contentDescription = "Logo Chile Huerta",
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
        )


        // 2. Título "Iniciar Sesión"
        Text(
            text = "Iniciar Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // 3. Campo de Usuario
        Text(
            text = "Usuario",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingrese su usuario o correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(16.dp))


        // 4. Campo de Contraseña
        Text(
            text = "Contraseña",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingrese su contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            // Oculta el texto de la contraseña
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )


        Spacer(modifier = Modifier.height(32.dp))


        // 5. Botón "Inicio de sesión"
        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0D6F5) // Color lila
            )
        ) {
            Text(text = "Inicio de sesión", color = Color.DarkGray.copy(alpha = 0.8f))
        }


        Spacer(modifier = Modifier.height(24.dp))


        // 6. Botón "Crear una cuenta"
        Text(text = "¿No tienes un cuenta?")
        TextButton(onClick = onCreateAccountClick) {
            // El botón de "Crear cuenta" en tu imagen también es lila
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


// Vista previa para esta pantalla
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SignInScreenPreview() {
    HuertoHogarTiendaAppTheme {
        SignInScreen(onSignInClick = {}, onCreateAccountClick = {})
    }
}