package com.example.huertohogartiendaapp.data

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: Long = 0L,
    val stock: Long = 0L, // <--- CAMBIO CLAVE: De Int a Long
    val descripcion: String = "",
    val categoria: String = "",
    val imagen: String = ""
)
