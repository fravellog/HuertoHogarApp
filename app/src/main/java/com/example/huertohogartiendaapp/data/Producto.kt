package com.example.huertohogartiendaapp.data


// data class es perfecta para guardar datos
data class Producto(
    val id: String,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val descripcion: String,
    val categoria: String // Ej: "Frutas" o "Verduras"
    // val imagenUrl: String
)