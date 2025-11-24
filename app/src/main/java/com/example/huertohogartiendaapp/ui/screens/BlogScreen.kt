package com.example.huertohogartiendaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// --- IMPORTS AÑADIDOS PARA QUE FUNCIONE ---
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.data.BlogRepository
import com.example.huertohogartiendaapp.data.EntradaBlog
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme

@Composable
fun BlogScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // Eliminamos el padding superior para que el banner empiece desde arriba
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- BANNER REUTILIZABLE AÑADIDO ---
        item {
            // 1. CORRECCIÓN: Usamos 'placeholder_banner' que ya existe.
            // Si creas una imagen 'blog_banner.png', puedes volver a cambiarlo.
            ScreenBanner(titulo = "Nuestro Blog", imagenRes = R.drawable.placeholder_banner)
        }

        items(BlogRepository.todasLasEntradas) { entrada ->
            // 2. CORRECCIÓN: La función 'BlogCard' ahora está definida abajo.
            BlogCard(entrada = entrada)
        }
    }
}

// --- FUNCIÓN 'BlogCard' AÑADIDA DENTRO DEL MISMO ARCHIVO ---
@Composable
private fun BlogCard(entrada: EntradaBlog) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp) // Añadimos padding horizontal para que no pegue a los bordes
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = entrada.imagen),
                contentDescription = "Imagen para ${entrada.titulo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = entrada.titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Por ${entrada.autor} - ${entrada.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = entrada.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3 // Mostramos solo un resumen
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = { /* TODO: Navegar a la vista detallada del blog */ }) {
                    Text("LEER MÁS")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BlogScreenPreview() {
    HuertoHogarTiendaAppTheme {
        BlogScreen()
    }
}
