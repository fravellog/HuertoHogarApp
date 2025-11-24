package com.example.huertohogartiendaapp.data

import androidx.annotation.DrawableRes

data class EntradaBlog(
    val id: String,
    val titulo: String,
    val autor: String,
    val fecha: String,
    @DrawableRes val imagen: Int,
    val contenido: String
)
