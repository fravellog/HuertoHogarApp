package com.example.huertohogartiendaapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Es una 'class' que se encargará de la lógica de datos de productos.
class ProductoRepository {

    // Obtenemos la instancia de la base de datos de Firestore
    private val db = FirebaseFirestore.getInstance()

    // --- CORRECCIÓN CLAVE: La función ahora devuelve un Flow ---
    // Un Flow es una corriente de datos que puede emitir valores con el tiempo.
    fun getTodosLosProductosEnTiempoReal(): Flow<List<Producto>> = callbackFlow {
        // 1. Nos conectamos a la colección "productos" en Firestore
        val listener = db.collection("productos")
            .addSnapshotListener { snapshot, error ->
                // 2. Este bloque de código se ejecutará CADA VEZ que haya un cambio en Firestore.

                // Si hay un error de Firebase (ej: sin permisos), cerramos el Flow con el error.
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                // Si el snapshot es nulo o no existe, enviamos una lista vacía.
                if (snapshot == null || snapshot.isEmpty) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // 3. Convertimos los documentos de Firebase a nuestra lista de Productos.
                val productos = snapshot.toObjects(Producto::class.java)

                // 4. Enviamos la nueva lista de productos a través de la "tubería" (el Flow).
                trySend(productos)
            }

        // 5. Esto es crucial: cuando el ViewModel deje de escuchar (ej: la app se cierra),
        // el listener de Firebase se eliminará para ahorrar batería y datos.
        awaitClose { listener.remove() }
    }
}
