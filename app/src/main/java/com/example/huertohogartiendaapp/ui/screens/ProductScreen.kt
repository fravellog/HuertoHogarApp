package com.example.huertohogartiendaapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.R
import com.example.huertohogartiendaapp.util.formatToCLP

@Composable
fun ProductScreen(
    productId: String,
    mainViewModel: MainViewModel = viewModel()
) {
    val producto by remember {
        derivedStateOf { mainViewModel.getProductById(productId) }
    }

    if (producto == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val context = LocalContext.current
        val imageName = producto!!.imagen.trim().lowercase()
        val imageResId = remember(imageName) {
            context.resources.getIdentifier(imageName, "drawable", context.packageName)
        }

        // LOG DE DIAGNÓSTICO
        Log.d("ImageLoading", "ProductScreen - Producto: ${producto!!.nombre}, Buscando drawable: '$imageName', ID Encontrado: $imageResId")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.placeholder_banner),
                contentDescription = producto!!.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = producto!!.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = producto!!.categoria,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = producto!!.descripcion,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatToCLP(producto!!.precio),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Stock: ${producto!!.stock}",
                        fontSize = 16.sp,
                        color = if (producto!!.stock > 0) MaterialTheme.colorScheme.onSurface else Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { mainViewModel.agregarAlCarrito(producto!!) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = producto!!.stock > 0
                ) {
                    Text("AGREGAR AL CARRITO")
                }
            }
        }
    }
}
