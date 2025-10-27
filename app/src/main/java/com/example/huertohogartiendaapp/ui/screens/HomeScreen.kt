package com.example.huertohogartiendaapp.ui.screens



import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

// Importamos el ViewModel

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.huertohogartiendaapp.R

import com.example.huertohogartiendaapp.data.Producto // Importamos Producto

import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme



// --- Pantalla Principal (Contenedor) ---

@Composable

fun HomeScreen(

    // AHORA RECIBE LA FUNCIÓN DE NAVEGACIÓN

    onNavigate: (String) -> Unit

) {

    // Obtenemos el ViewModel para las ofertas

    val viewModel: MainViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()



    Scaffold(

        // Le pasamos la función de navegación a la barra

        bottomBar = { AppBottomBar(selectedItem = -1, onNavigate = onNavigate) } // -1 = Ninguno

    ) { innerPadding ->



        LazyColumn(

            modifier = Modifier

                .fillMaxSize()

                .padding(innerPadding),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {



            item {

                Image(

                    painter = painterResource(id = R.drawable.placeholder_banner),

                    contentDescription = "Banner de Verduras",

                    modifier = Modifier

                        .fillMaxWidth()

                        .height(200.dp),

                    contentScale = ContentScale.Crop

                )

            }



            item {

                Text(

                    text = "Ofertas",

                    fontSize = 26.sp,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.padding(vertical = 16.dp)

                )

            }



            // Mostramos las 3 ofertas del ViewModel

            items(uiState.productosMuestra) { producto ->

                OfferItem(

                    imageRes = producto.imagenRes,

                    title = producto.nombre,

                    price = "Precio: $${producto.precio}"

                )

            }



            item {

                Button(

                    // El botón "Ver productos" ahora usa la función de navegación

                    onClick = { onNavigate("products") },

                    modifier = Modifier

                        .fillMaxWidth(0.7f)

                        .padding(vertical = 24.dp)

                        .height(50.dp),

                    colors = ButtonDefaults.buttonColors(

                        containerColor = Color(0xFFE0D6F5)

                    )

                ) {

                    Text(text = "Ver productos", color = Color.DarkGray.copy(alpha = 0.8f))

                }

            }

        }

    }

}



// --- Barra de Navegación Inferior ---

@Composable

fun AppBottomBar(

    selectedItem: Int, // Para saber cuál marcar

    onNavigate: (String) -> Unit // Para navegar

) {

    NavigationBar(

        containerColor = Color(0xFFDCEFDC)

    ) {

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Storefront, contentDescription = "Tienda") },

            label = { Text("Tienda") },

            selected = selectedItem == 0,

            onClick = { onNavigate("products") } // <-- ACCIÓN AÑADIDA

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Article, contentDescription = "Blog") },

            label = { Text("Blog") },

            selected = selectedItem == 1,

            onClick = { /* onNavigate("blog") */ } // (Para el futuro)

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },

            label = { Text("Perfil") },

            selected = selectedItem == 2,

            onClick = { /* onNavigate("profile") */ } // (Para el futuro)

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Email, contentDescription = "Contacto") },

            label = { Text("Contacto") },

            selected = selectedItem == 3,

            onClick = { /* onNavigate("contact") */ } // (Para el futuro)

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito") },

            label = { Text("Carrito") },

            selected = selectedItem == 4,

            onClick = { /* onNavigate("cart") */ } // (Para el futuro)

        )

    }

}



// --- Item de Oferta Reutilizable ---

@Composable

fun OfferItem(

    imageRes: Int,

    title: String,

    price: String

) {

    // (Este código no cambia, pero es necesario tenerlo)

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.padding(bottom = 8.dp)

    ) {

        Surface(

            modifier = Modifier

                .fillMaxWidth(0.9f)

                .height(100.dp),

            shape = MaterialTheme.shapes.medium,

            border = BorderStroke(2.dp, Color(0xFFA5D6A7))

        ) {

            Row(

                modifier = Modifier.fillMaxSize().padding(8.dp),

                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(

                    imageVector = Icons.Filled.ShoppingCart, // Icono de carrito estándar

                    contentDescription = "Carrito",

                    modifier = Modifier.size(60.dp),

                    tint = Color(0xFF388E3C)

                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(

                    modifier = Modifier.weight(1f),

                    verticalArrangement = Arrangement.Center

                ) {

                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Text(text = price, fontSize = 14.sp)

                }

                Image(

                    painter = painterResource(id = imageRes),

                    contentDescription = title,

                    modifier = Modifier.size(80.dp)

                )

            }

        }



        Spacer(modifier = Modifier.height(8.dp))



        Button(

            onClick = { /* Lógica para agregar al carrito */ },

            modifier = Modifier

                .fillMaxWidth(0.6f)

                .height(40.dp),

            colors = ButtonDefaults.buttonColors(

                containerColor = Color(0xFFE0D6F5)

            )

        ) {

            Text(text = "Agregar al Carrito", color = Color.DarkGray.copy(alpha = 0.8f), fontSize = 12.sp)

        }

    }

}





@Preview(showBackground = true, device = "id:pixel_6")

@Composable

fun HomeScreenPreview() {

    HuertoHogarTiendaAppTheme {

        HomeScreen(onNavigate = {})

    }

}