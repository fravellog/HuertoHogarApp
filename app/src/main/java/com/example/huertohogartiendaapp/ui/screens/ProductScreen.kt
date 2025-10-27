package com.example.huertohogartiendaapp.ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme



// Esta es la pantalla que se muestra cuando navegas a "products"

@Composable

fun ProductScreen(

    // Recibirá el controlador de navegación para que su barra inferior funcione

    onNavigate: (String) -> Unit

) {

    val viewModel: MainViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()



    Scaffold(
        // Reutilizamos la barra de navegación inferior
        bottomBar = { AppBottomBar(selectedItem = 0, onNavigate = onNavigate) } // 0 es "Tienda"
    ) { innerPadding ->

        // Fila principal que contiene las dos columnas
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            // Columna de Verduras (con scroll)

            ProductColumn(

                title = "Verduras",

                products = uiState.verduras,

                modifier = Modifier.weight(1f) // Ocupa la mitad del espacio

            )



            Spacer(modifier = Modifier.width(8.dp))



            // Columna de Frutas (con scroll)

            ProductColumn(

                title = "Frutas",

                products = uiState.frutas,

                modifier = Modifier.weight(1f) // Ocupa la otra mitad

            )

        }

    }

}



// Composable para una columna de productos (reutilizable)

@Composable

fun ProductColumn(

    title: String,

    products: List<Producto>,

    modifier: Modifier = Modifier

) {

    LazyColumn(

        modifier = modifier,

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        item {

            Text(

                text = title,

                fontSize = 22.sp,

                fontWeight = FontWeight.Bold,

                modifier = Modifier.padding(vertical = 8.dp)

            )

        }

        items(products) { producto ->

            ProductStoreItem(product = producto)

            Spacer(modifier = Modifier.height(16.dp))

        }

    }

}



// Composable para la tarjeta de producto en la tienda

@Composable

fun ProductStoreItem(

    product: Producto

) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = product.nombre, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        Text(text = "Precio: $${product.precio}", fontSize = 12.sp)

        Image(

            painter = painterResource(id = product.imagenRes),

            contentDescription = product.nombre,

            modifier = Modifier

                .fillMaxWidth()

                .aspectRatio(1.5f) // Proporción para que la imagen no sea gigante

                .padding(vertical = 4.dp),

            contentScale = ContentScale.Crop

        )

        Button(

            onClick = { /* Lógica para agregar al carrito */ },

            modifier = Modifier

                .fillMaxWidth(0.9f)

                .height(35.dp),

            colors = ButtonDefaults.buttonColors(

                containerColor = Color(0xFFE0D6F5) // Color lila

            ),

            contentPadding = PaddingValues(0.dp)

        ) {

            Text(text = "Agregar al Carrito", fontSize = 10.sp, color = Color.DarkGray.copy(alpha = 0.8f))

        }

    }

}





@Preview(showBackground = true)

@Composable

fun ProductScreenPreview() {

    HuertoHogarTiendaAppTheme {

        // Simulación para la vista previa

        ProductScreen(onNavigate = {})

    }

}