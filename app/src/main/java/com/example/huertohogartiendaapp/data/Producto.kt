package com.example.huertohogartiendaapp.data

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val descripcion: String = "",
    val categoria: String = "",
    // --- CORRECCIÓN CLAVE ---
    // El nombre de la propiedad ahora coincide con el campo de Firebase.
    val imagen: String = ""
)


