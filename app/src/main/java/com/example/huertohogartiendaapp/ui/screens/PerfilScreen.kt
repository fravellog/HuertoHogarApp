package com.example.huertohogartiendaapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.huertohogartiendaapp.R

@Composable
fun PerfilScreen(
    mainViewModel: MainViewModel = viewModel(),
    onLogout: () -> Unit,
    onNavigateToDireccion: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            mainViewModel.onFotoPerfilCambiada(uri)
        }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        // Foto de Perfil
        item {
            ProfileImage(uiState.fotoPerfilUri) {
                imagePickerLauncher.launch("image/*")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Nombre de Usuario
        item {
            Text(
                text = uiState.loginEmailOrUsername, // O el nombre de usuario real
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.loginEmailOrUsername, // O el email real
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Opciones de menú
        item {
            ProfileMenuItem("Mi Dirección de Envío", onNavigateToDireccion)
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        // Botón de Cerrar Sesión
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                Spacer(modifier = Modifier.width(8.dp))
                Text("CERRAR SESIÓN")
            }
        }
    }
}

@Composable
fun ProfileImage(imageUri: Uri?, onImageClick: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = if (imageUri != null) rememberAsyncImagePainter(imageUri) else painterResource(id = R.drawable.placeholder_banner),
            contentDescription = "Foto de Perfil",
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onImageClick),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Cambiar Foto",
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(6.dp),
            tint = Color.White
        )
    }
}

@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn, // Icono de ejemplo
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
}
