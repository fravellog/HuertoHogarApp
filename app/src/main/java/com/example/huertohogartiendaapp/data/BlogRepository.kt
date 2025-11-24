package com.example.huertohogartiendaapp.data

import com.example.huertohogartiendaapp.R

object BlogRepository {

    val todasLasEntradas = listOf(
        EntradaBlog(
            id = "b1",
            titulo = "10 Consejos para Iniciar tu Huerto en Casa",
            autor = "Ana Verde",
            fecha = "15 de Octubre, 2025",
            imagen = R.drawable.blog_iniciar_huerto, // Necesitarás añadir esta imagen
            contenido = "Iniciar un huerto en casa puede parecer un desafío, pero con estos 10 consejos, estarás cosechando tus propias verduras en poco tiempo. Desde la elección de las macetas hasta el riego adecuado..."
        ),
        EntradaBlog(
            id = "b2",
            titulo = "Cómo Combatir Plagas de Forma Orgánica",
            autor = "Carlos Tierra",
            fecha = "05 de Noviembre, 2025",
            imagen = R.drawable.blog_plagas, // Necesitarás añadir esta imagen
            contenido = "Olvídate de los químicos dañinos. Aprende a crear tus propios pesticidas orgánicos y a utilizar insectos beneficiosos para mantener tu huerto sano y libre de plagas."
        ),
        EntradaBlog(
            id = "b3",
            titulo = "La Guía Definitiva para el Compostaje Casero",
            autor = "Sofía Abono",
            fecha = "20 de Noviembre, 2025",
            imagen = R.drawable.blog_compost, // Necesitarás añadir esta imagen
            contenido = "Transforma los desechos de tu cocina en 'oro negro' para tus plantas. Te guiamos paso a paso en el proceso de compostaje, desde qué puedes compostar hasta cómo saber cuándo está listo."
        )
    )
}
