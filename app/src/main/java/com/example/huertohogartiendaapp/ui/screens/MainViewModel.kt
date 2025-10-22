package com.example.huertohogartiendaapp.ui.screens


import androidx.lifecycle.ViewModel
import com.example.huertohogartiendaapp.data.Producto
import com.example.huertohogartiendaapp.data.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


// Estado de la UI: Guarda las listas que la pantalla necesita
data class MainUiState(
    val productosMuestra: List<Producto> = emptyList(),
    val productosSeccion: List<Producto> = emptyList()
)


class MainViewModel : ViewModel() {


    // 1. Llama al Repositorio
    private val repository = ProductoRepository


    // 2. Prepara el "Estado" (UiState) que la vista observará
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    // 3. El bloque init se ejecuta cuando el ViewModel es creado
    init {
        cargarProductos()
    }


    private fun cargarProductos() {
        // Obtiene los datos del repositorio
        val muestra = repository.getProductosDeMuestra()
        val todos = repository.getTodosLosProductos()


        // Actualiza el estado
        _uiState.update { estadoActual ->
            estadoActual.copy(
                productosMuestra = muestra,
                productosSeccion = todos
            )
        }
    }
}