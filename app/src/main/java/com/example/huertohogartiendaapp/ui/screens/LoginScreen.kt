package com.example.huertohogartiendaapp.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// ¡Importante para que R.drawable funcione!
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme
@Composable
fun LoginScreen(
    // Este parámetro nos servirá para notificar
    // a la navegación que debe cambiar de pantalla
    onLoginClick: () -> Unit
) {
    // Estado para el checkbox, se maneja solo dentro de esta pantalla
    var isChecked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // Centra todo verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra todo horizontalmente
    ) {
        // 1. Texto de bienvenida
        Text(
            text = "Bienvenidos a",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        // 2. El Logo
        Image(
            // Usa el logo que pegaste en res/drawable
            painter = painterResource(id = R.drawable.logo_huerta), // (Asegúrate que tu logo se llame así)
            contentDescription = "Logo Chile Huerta",
            modifier = Modifier
                .fillMaxWidth(0.9f) // Ocupa el 90% del ancho
                .padding(vertical = 24.dp)
        )
        // 3. El Checkbox y el Texto
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange= { isChecked = it } // (Este era uno de los errores)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Aceptar términos y condiciones")
        }


        // 4. El Botón de Inicio de Sesión
        Button(
            onClick = onLoginClick, // (Este era otro error)
            enabled = isChecked,
            modifier = Modifier
                .fillMaxWidth(0.7f) // Ocupa el 70% del ancho
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0D6F5),
                disabledContainerColor = Color(0xFFE0D6F5).copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Inicio de sesión",
                color = Color.DarkGray.copy(alpha = 0.8f)
            )
        }
    }
}
// Vista previa para ver tu diseño sin ejecutar la app
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun LoginScreenPreview() {
    HuertoHogarTiendaAppTheme {
        LoginScreen(onLoginClick = {})
    }
}