package com.example.huertohogartiendaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Imports de Navegación
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Imports de tus pantallas
import com.example.huertohogartiendaapp.ui.screens.LoginScreen
import com.example.huertohogartiendaapp.ui.screens.ProductScreen
import com.example.huertohogartiendaapp.ui.screens.SignInScreen
import com.example.huertohogartiendaapp.ui.screens.RegisterScreen // <-- 1. IMPORTA LA NUEVA PANTALLA
import com.example.huertohogartiendaapp.ui.theme.HuertoHogarTiendaAppTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HuertoHogarTiendaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla de Bienvenida
        composable(route = "login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("signin")
                }
            )
        }
        // Pantalla de Iniciar Sesión
        composable(route = "signin") {
            SignInScreen(
                onSignInClick = {
                    navController.navigate("products")
                },
                // 2. AQUÍ CONECTAMOS EL BOTÓN
                onCreateAccountClick = {
                    navController.navigate("register") // Te lleva a la nueva ruta
                }
            )
        }
        // 3. AÑADE LA NUEVA RUTA "register"
        composable(route = "register") {
            RegisterScreen(
                onRegisterClick = {
                    // Al registrarse, lo devolvemos a la pantalla de login
                    // para que ahora inicie sesión.
                    navController.popBackStack() // popBackStack() vuelve a la pantalla anterior (signin)
                }
            )
        }
        // Pantalla de Productos

        composable(route = "products") {

            ProductScreen()

        }
    }
}