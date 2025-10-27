package com.example.huertohogartiendaapp.data

import androidx.annotation.DrawableRes


// data class es perfecta para guardar datos
data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val descripcion: String,
    val categoria: String, // Ej: "Frutas" o "Verduras"
    @DrawableRes val imagenRes : Int// Para mostrar la imagen
    // val imagenUrl: String
)