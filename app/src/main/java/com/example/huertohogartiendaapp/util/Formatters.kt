package com.example.huertohogartiendaapp.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formatea un número Double al estilo de moneda chilena (CLP).
 * Ej: 1500.0 -> "$1.500"
 * Ej: 990.0 -> "$990"
 *
 * @param precio El número a formatear.
 * @return Un String con el precio formateado en CLP.
 */
fun formatToCLP(precio: Double): String {
    // Usamos el Locale de español-Chile para obtener el formato correcto.
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    // El formato chileno por defecto puede incluir decimales, así que los quitamos.
    format.maximumFractionDigits = 0
    return format.format(precio)
}
