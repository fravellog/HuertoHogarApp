package com.example.huertohogartiendaapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertohogartiendaapp.ui.screens.*
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

        composable(route = "login") {
            LoginScreen(
                onLoginClick = { navController.navigate("signin") }
            )
        }


        composable(route = "signin") {
            SignInScreen(
                onSignInClick = {
                    navController.navigate("home") {
                        popUpTo("signin") { inclusive = true }
                    }
                },
                onCreateAccountClick = { navController.navigate("register") }
            )
        }


        composable(route = "register") {
            RegisterScreen(
                onRegisterClick = { navController.popBackStack() }
            )
        }

        composable(route = "home") {
            HomeScreen(
                // Pasamos la función de navegación
                onNavigate= { ruta -> navController.navigate(ruta) }
            )
        }

        composable(route = "products") {
            ProductScreen(
                // Pasamos la función de navegación
                onNavigate = { ruta -> navController.navigate(ruta) }
            )
        }
    }
}