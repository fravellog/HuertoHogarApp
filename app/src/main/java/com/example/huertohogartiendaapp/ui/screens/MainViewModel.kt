package com.example.huertohogartiendaapp.ui.screens


import androidx.lifecycle.ViewModel
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.data.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


// Estado de la UI: Guarda todas las listas que necesitamos
data class MainUiState(
    val productosMuestra: List<Producto> = emptyList(), // Para HomeScreen
    val verduras: List<Producto> = emptyList(),         // Para TiendaScreen
    val frutas: List<Producto> = emptyList()            // Para TiendaScreen
)


class MainViewModel : ViewModel() {


    private val repository = ProductoRepository


    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    init {
        cargarProductos()
    }


    private fun cargarProductos() {
        val muestra = repository.getProductosDeMuestra()
        val verduras = repository.getVerduras()
        val frutas = repository.getFrutas()


        _uiState.update { estadoActual ->
            estadoActual.copy(
                productosMuestra = muestra,
                verduras = verduras,
                frutas = frutas
            )
        }
    }
}