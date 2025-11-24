package com.example.huertohogartiendaapp.remote

import com.example.huertohogartiendaapp.data.NewsApiResponse
import com.example.huertohogartiendaapp.data.Producto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

// --- CONFIGURACIÓN PARA TUS MICROSERVICIOS ---
// ¡¡¡USA LA IP REAL DE TU COMPUTADOR!!! (la que te dio ipconfig)
// NO uses 10.0.2.2. Usa la que empieza con 192.168...
private const val MI_API_BASE_URL = "http://192.168.2.111:8081/" // <-- ¡¡PUSA TU IP REAL AQUÍ!!

private val miApiRetrofit = Retrofit.Builder()
    .baseUrl(MI_API_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// --- CONFIGURACIÓN PARA LA API DE NOTICIAS ---
private const val NEWS_API_BASE_URL = "https://newsapi.org/"
private val newsApiRetrofit = Retrofit.Builder()
    .baseUrl(NEWS_API_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @GET("api/productos")
    suspend fun getProductos(): List<Producto>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "es",
        @Query("sortBy") sortBy: String = "publishedAt"
    ): NewsApiResponse
}

object Api {
    val service: ApiService by lazy {
        miApiRetrofit.create(ApiService::class.java)
    }
    val newsApiService: ApiService by lazy {
        newsApiRetrofit.create(ApiService::class.java)
    }
}
