package com.example.huertohogartiendaapp.data


import com.example.huertohogartiendaapp.R


object ProductoRepository {


    // Lista completa con todas las imágenes
    private val todosLosProductos = listOf(
        // Verduras
        Producto("V01", "Brocoli", 1490.0, 100, "Brocoli fresco", "Verduras", R.drawable.brocoli),
        Producto("V02", "Lechuga", 1180.0, 150, "Lechuga fresca c/u", "Verduras", R.drawable.lechuga),
        Producto("V03", "Papas", 1350.0, 200, "Papas /kg", "Verduras", R.drawable.papa),
        Producto("V04", "Cebollas", 1420.0, 180, "Cebollas /kg", "Verduras", R.drawable.cebolla),
        Producto("V05", "Tomate", 1610.0, 130, "Tomate /kg", "Verduras", R.drawable.tomate),
        Producto("V06", "Zanahoria", 1200.0, 160, "Zanahoria /kg", "Verduras", R.drawable.zanahoria),

        // Frutas
        Producto("F01", "Manzana", 1780.0, 200, "Manzana roja /kg", "Frutas", R.drawable.manzana),
        Producto("F02", "Mango", 1570.0, 100, "Mango /kg", "Frutas", R.drawable.mango),
        Producto("F03", "Frutilla", 3990.0, 80, "Frutillas /kg", "Frutas", R.drawable.frutilla),
        Producto("F04", "Naranja", 1720.0, 150, "Naranjas /kg", "Frutas", R.drawable.naranja),
        Producto("F05", "Platanos", 1400.0, 250, "Platanos /kg", "Frutas", R.drawable.platanos),
        Producto("F06", "Sandia", 3500.0, 50, "Sandia c/u", "Frutas", R.drawable.sandia)
    )


    // Función para obtener solo las Verduras
    fun getVerduras(): List<Producto> {
        return todosLosProductos.filter { it.categoria == "Verduras" }
    }


    // Función para obtener solo las Frutas
    fun getFrutas(): List<Producto> {
        return todosLosProductos.filter { it.categoria == "Frutas" }
    }

    // Función para las ofertas de la HomeScreen (la mantenemos)
    fun getProductosDeMuestra(): List<Producto> {
        // Puedes elegir cuáles mostrar, ej: lechuga, tomate, zanahoria
        return listOf(
            todosLosProductos.find { it.id == "V02" }!!,
            todosLosProductos.find { it.id == "V05" }!!,
            todosLosProductos.find { it.id == "V06" }!!
        )
    }
}