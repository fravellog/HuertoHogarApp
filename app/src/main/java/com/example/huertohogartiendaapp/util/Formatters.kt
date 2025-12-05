package com.example.huertohogartiendaapp.util

import java.text.NumberFormat
import java.util.Locale

fun formatToCLP(price: Number): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0 // Opcional: si no quieres decimales
    return format.format(price)
}
