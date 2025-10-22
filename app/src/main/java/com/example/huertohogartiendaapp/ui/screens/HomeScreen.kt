package com.example.huertohogartiendaapp.ui.screens



import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Article

import androidx.compose.material.icons.filled.Email

import androidx.compose.material.icons.filled.Person

import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material.icons.filled.Storefront

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

import com.example.huertohogartiendaapp.R

import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme



// --- Pantalla Principal (Contenedor) ---

@Composable

fun HomeScreen(

    onViewProductsClick: () -> Unit

) {

    // Scaffold nos da la estructura para la barra de navegación inferior

    Scaffold(

        bottomBar = { AppBottomBar() }

    ) { innerPadding ->



        // Usamos LazyColumn para que la pantalla sea "scrollable"

        LazyColumn(

            modifier = Modifier

                .fillMaxSize()

                .padding(innerPadding), // Padding para que no se solape con la barra inferior

            horizontalAlignment = Alignment.CenterHorizontally

        ) {



            // 1. Banner Superior

            item {

                Image(

                    painter = painterResource(id = R.drawable.banner_verduras),

                    contentDescription = "Banner de Verduras",

                    modifier = Modifier

                        .fillMaxWidth()

                        .height(200.dp),

                    contentScale = ContentScale.Crop // Asegura que la imagen cubra el espacio

                )

            }



            // 2. Título "Ofertas"

            item {

                Text(

                    text = "Ofertas",

                    fontSize = 26.sp,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.padding(vertical = 16.dp)

                )

            }



            // 3. Lista de Ofertas

            item {

                OfferItem(

                    imageRes = R.drawable.lechuga,

                    title = "Lechuga",

                    price = "Precio: $1.180 c/u"

                )

            }

            item {

                OfferItem(

                    imageRes = R.drawable.tomate,

                    title = "Tomate",

                    price = "Precio: $1.610 /kg"

                )

            }

            item {

                OfferItem(

                    imageRes = R.drawable.zanahoria,

                    title = "Zanahoria",

                    price = "Precio: $1.200 /kg"

                )

            }



            // 4. Botón "Ver productos"

            item {

                Button(

                    onClick = onViewProductsClick,

                    modifier = Modifier

                        .fillMaxWidth(0.7f)

                        .padding(vertical = 24.dp)

                        .height(50.dp),

                    colors = ButtonDefaults.buttonColors(

                        containerColor = Color(0xFFE0D6F5) // Color lila

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

fun AppBottomBar() {

    // Estado para saber qué ícono está seleccionado

    var selectedItem by remember { mutableStateOf(0) }



    NavigationBar(

        containerColor = Color(0xFFDCEFDC) // Color verde claro de fondo

    ) {

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Storefront, contentDescription = "Tienda") },

            label = { Text("Tienda") },

            selected = selectedItem == 0,

            onClick = { selectedItem = 0 }

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Article, contentDescription = "Blog") },

            label = { Text("Blog") },

            selected = selectedItem == 1,

            onClick = { selectedItem = 1 }

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },

            label = { Text("Perfil") },

            selected = selectedItem == 2,

            onClick = { selectedItem = 2 }

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.Email, contentDescription = "Contacto") },

            label = { Text("Contacto") },

            selected = selectedItem == 3,

            onClick = { selectedItem = 3 }

        )

        NavigationBarItem(

            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito") },

            label = { Text("Carrito") },

            selected = selectedItem == 4,

            onClick = { selectedItem = 4 }

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

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.padding(bottom = 8.dp)

    ) {

        // Tarjeta verde

        Surface(

            modifier = Modifier

                .fillMaxWidth(0.9f)

                .height(100.dp),

            shape = MaterialTheme.shapes.medium,

            border = BorderStroke(2.dp, Color(0xFFA5D6A7)) // Borde verde

        ) {

            Row(

                modifier = Modifier.fillMaxSize().padding(8.dp),

                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(

                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Icono de carrito verde (placeholder)

                    contentDescription = "Carrito",

                    modifier = Modifier.size(60.dp),

                    tint = Color(0xFF388E3C)

                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(

                    modifier = Modifier.weight(1f), // Ocupa el espacio restante

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



        // Botón "Agregar al Carrito"

        Button(

            onClick = { /* Lógica para agregar al carrito */ },

            modifier = Modifier

                .fillMaxWidth(0.6f)

                .height(40.dp),

            colors = ButtonDefaults.buttonColors(

                containerColor = Color(0xFFE0D6F5) // Color lila

            )

        ) {

            Text(text = "Agregar al Carrito", color = Color.DarkGray.copy(alpha = 0.8f), fontSize = 12.sp)

        }

    }

}



// --- Vista Previa ---

@Preview(showBackground = true, device = "id:pixel_6")

@Composable

fun HomeScreenPreview() {

    HuertoHogarTiendaAppTheme {

        HomeScreen(onViewProductsClick = {})

    }

}