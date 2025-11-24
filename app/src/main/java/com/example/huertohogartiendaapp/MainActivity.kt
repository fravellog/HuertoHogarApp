package com.example.huertohogartiendaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
// --- IMPORTACIÓN CORRECTA Y SIMPLIFICADA ---
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
                    AppShell()
                }
            }
        }
    }
}

@Composable
fun AppShell() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val uiState by mainViewModel.uiState.collectAsState()

    val screensWithBottomBar = listOf("home", "tienda", "blog", "perfil", "carrito")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            mainViewModel.snackbarMostrado()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login", // Cambia a "login" o "signin" según tu flujo inicial preferido
            modifier = Modifier.padding(innerPadding)
        ) {
            // PANTALLAS DE AUTENTICACIÓN
            composable("login") {
                SignInScreen(
                    mainViewModel = mainViewModel,
                    onLoginSuccess = {
                        // No es necesario llamar a mainViewModel.loginCompleted() aquí,
                        // la navegación se encarga.
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onCreateAccountClick = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    mainViewModel = mainViewModel,
                    onRegistrationSuccess = {
                        // Al registrarse con éxito, vuelve a la pantalla de login
                        navController.popBackStack()
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            // PANTALLAS PRINCIPALES
            composable("home") { HomeScreen(mainViewModel = mainViewModel) }
            composable("tienda") { TiendaScreen(mainViewModel = mainViewModel) }
            composable("blog") { BlogScreen() }
            composable("carrito") { CarritoScreen(mainViewModel = mainViewModel, navController = navController) }
            composable("perfil") {
                PerfilScreen(
                    mainViewModel = mainViewModel,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true } // Borra todo el historial de navegación
                        }
                    },
                    // --- CORRECCIÓN AQUÍ ---
                    // Añadimos el parámetro que faltaba
                    onNavigateToDireccion = { navController.navigate("direccion") }
                )
            }

            // --- NUEVA PANTALLA ---
            composable("direccion") {
                DireccionScreen(
                    mainViewModel = mainViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(containerColor = Color(0xFFDCEFDC)) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = { onNavigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Storefront, "Tienda") },
            label = { Text("Tienda") },
            selected = currentRoute == "tienda",
            onClick = { onNavigate("tienda") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.Article, "Blog") },
            label = { Text("Blog") },
            selected = currentRoute == "blog",
            onClick = { onNavigate("blog") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = { onNavigate("perfil") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, "Carrito") },
            label = { Text("Carrito") },
            selected = currentRoute == "carrito",
            onClick = { onNavigate("carrito") }
        )
    }
}
