package com.example.huertohogartiendaapp.ui.screens // ¡Verifica tu paquete!

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. (Opcional) Define una clase de datos para tus posts
data class BlogPost(
    val id: Int,
    val title: String,
    val description: String,
    val authorInitial: String = "A" // Para el círculo
)

// 2. Datos de ejemplo para llenar la cuadrícula
val dummyBlogList = listOf(
    BlogPost(1, "Tema 1", "Descripción"),
    BlogPost(2, "Tema 2", "Descripción"),
    BlogPost(3, "Tema 3", "Descripción"),
    BlogPost(4, "Tema 4", "Descripción"),
    BlogPost(5, "Tema 5", "Descripción"),
    BlogPost(6, "Tema 6", "Descripción")
)

@Composable
fun BlogScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Título "Blog"
        Text(
            text = "Blog",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp), // Espacio arriba y abajo
            textAlign = TextAlign.Center // Centrado
        )

        // 3. La cuadrícula (Grid) perezosa
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Siempre 2 columnas
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(bottom = 12.dp), // Espacio al final de la lista
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Espacio horizontal entre tarjetas
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio vertical entre tarjetas
        ) {
            // Itera sobre la lista de posts y crea una tarjeta para cada uno
            items(dummyBlogList) { post ->
                BlogItemCard(post = post)
            }
        }
    }
}

// 4. El Composable para cada "tarjeta" del blog
@Composable
fun BlogItemCard(post: BlogPost) {
    // Surface o Card le da el fondo blanco y la forma
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Bordes redondeados
        shadowElevation = 4.dp, // Sombra ligera
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(12.dp) // Relleno interno de la tarjeta
        ) {
            // Fila para el Avatar (círculo) y el texto
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // El círculo "A"
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0FF)), // Color lila pálido
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorInitial,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5050A0) // Color lila oscuro
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Espacio

                // Columna para Título y Descripción
                Column {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espacio

            // El placeholder de la imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f) // Proporción (ancho 1, alto 1.2)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F5)), // Color gris pálido
                contentAlignment = Alignment.Center
            ) {
                // Puedes poner un icono o texto aquí si quieres
                Text("Imagen", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// 5. La previsualización
@Preview(showBackground = true)
@Composable
fun BlogScreenPreview() {
    BlogScreen()
}