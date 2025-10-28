package com.example.huertohogartiendaapp.ui.screens // ¡Asegúrate que este sea tu paquete correcto!

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField // Usaremos OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactoScreen() {
    // Estados para guardar lo que el usuario escribe en cada campo
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Relleno general para toda la pantalla
    ) {
        // Título "Contacto"
        Text(
            text = "Contacto",
            style = MaterialTheme.typography.headlineLarge.copy( // Cambiado a headlineLarge
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp // Un poco más grande
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp) // Espacio arriba y abajo del título
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el título y el primer campo

        // Campo Nombre
        Text(text = "Nombre", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingrese su nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Icono de persona") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos

        // Campo Correo electrónico
        Text(text = "Correo electrónico", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingrese su correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Icono de correo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos

        // Campo Mensaje (multilínea)
        Text(text = "Mensaje", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = mensaje,
            onValueChange = { mensaje = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), // Altura fija para el campo de mensaje
            placeholder = { Text("Ingrese un mensaje") },
            leadingIcon = { Icon(Icons.Default.Message, contentDescription = "Icono de mensaje") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false // Permite múltiples líneas
        )
    }
}

// Para ver una previsualización de tu diseño en Android Studio
@Preview(showBackground = true)
@Composable
fun ContactoScreenPreview() {
    ContactoScreen()
}