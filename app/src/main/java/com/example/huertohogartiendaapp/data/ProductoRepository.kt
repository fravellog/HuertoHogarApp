package com.example.huertohogartiendaapp.data


// Un 'object' es un Singleton, solo hay una instancia de él.
object ProductoRepository {


    // Lista privada con todos tus productos
    private val todosLosProductos = listOf(
        Producto("FR001", "Manzanas Fuji", 1200.0, 150, "Manzanas crujientes y dulces.", "Frutas"),
        Producto("FR002", "Naranjas Valencia", 1000.0, 200, "Jugosas y ricas en vitamina C.", "Frutas"),
        Producto("VR001", "Lechuga Costina", 900.0, 100, "Lechuga fresca para ensaladas.", "Verduras"),
        Producto("VR002", "Papas (Saco 5kg)", 5000.0, 80, "Papas ideales para freír o cocer.", "Verduras")
        // ... puedes agregar más si quieres
    )


    // Función para obtener TODOS los productos (para la "sección de productos")
    fun getTodosLosProductos(): List<Producto> {
        return todosLosProductos
    }


    // Función para obtener solo la MUESTRA (para la "pantalla principal")
    fun getProductosDeMuestra(): List<Producto> {
        // Tomamos solo los primeros 2 como muestra
        return todosLosProductos.take(2)
    }
}