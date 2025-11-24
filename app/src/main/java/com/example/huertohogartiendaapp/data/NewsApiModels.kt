package com.example.huertohogartiendaapp.data

// Representa un único artículo de noticia
data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val source: Source?
)

// Representa la fuente de la noticia dentro de un artículo
data class Source(
    val name: String?
)

// Representa la respuesta completa de la API
data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
