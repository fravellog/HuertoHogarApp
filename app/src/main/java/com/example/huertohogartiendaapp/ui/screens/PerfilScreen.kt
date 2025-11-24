package com.example.huertohogartiendaapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // <-- IMPORTANTE: para obtener el contexto
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.huertohogartiendaapp.MainViewModel
import com.example.huertohogartiendaapp.R

@Composable
fun PerfilScreen(
    mainViewModel: MainViewModel = viewModel(),
    onLogout: () -> Unit,
    onNavigateToDireccion: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    // --- CORRECCIÓN 1: Obtener el contexto actual ---
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        mainViewModel.onFotoPerfilCambiada(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Sección de Información del Usuario ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0D6F5))
                .padding(top = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                if (uiState.fotoPerfilUri != null) {
                    AsyncImage(
                        model = uiState.fotoPerfilUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_placeholder),
                        contentDescription = "Foto de perfil por defecto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.loginEmailOrUsername.substringBefore('@').replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.loginEmailOrUsername,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Menú de Opciones ---
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ProfileOptionItem(
                text = "Mis Pedidos",
                icon = Icons.AutoMirrored.Filled.ListAlt,
                onClick = { /* TODO: Navegar a la pantalla de historial de pedidos */ }
            )
            ProfileOptionItem(
                text = "Mi Dirección",
                icon = Icons.Default.LocationOn,
                onClick = onNavigateToDireccion
            )
            // --- CORRECCIÓN 2: Lógica para abrir el cliente de correo ---
            ProfileOptionItem(
                text = "Ayuda y Soporte",
                icon = Icons.Default.HelpOutline,
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:") // Solo apps de email deberían responder
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte.huerto@example.com", "contacto.equipo@example.com")) // Añade tus correos aquí
                        putExtra(Intent.EXTRA_SUBJECT, "Ayuda con la App Huerto Hogar")
                        putExtra(Intent.EXTRA_TEXT, "Hola, necesito ayuda con lo siguiente:\n\n")
                    }
                    // Inicia la actividad para abrir el cliente de correo
                    context.startActivity(Intent.createChooser(emailIntent, "Enviar correo usando..."))
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de cerrar sesión hacia abajo

        // --- Botón de Cerrar Sesión ---
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF5E0E0),
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
            Spacer(modifier = Modifier.width(8.dp))
            Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold)
        }
    }
}

// Composable reutilizable para cada opción del menú (se mantiene igual)
@Composable
fun ProfileOptionItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    // ... (el código de esta función no necesita cambios)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
