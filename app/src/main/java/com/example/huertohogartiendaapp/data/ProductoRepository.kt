package com.example.huertohogartiendaapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

class ProductoRepository {

    private val db = FirebaseFirestore.getInstance()

    private fun getProductosPorCategoria(categoria: String): Flow<List<Producto>> = callbackFlow {
        Log.d("Firestore", "Iniciando listener para la colección: $categoria")
        val listener = db.collection(categoria)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Error al escuchar la colección $categoria", error)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    Log.w("Firestore", "El snapshot de $categoria es nulo.")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                try {
                    val productos = snapshot.toObjects(Producto::class.java)
                    Log.d("Firestore", "Se cargaron ${productos.size} productos desde $categoria")
                    trySend(productos)
                } catch (e: Exception) {
                    Log.e("Firestore", "Error al convertir los productos de $categoria", e)
                    close(e)
                }
            }
        awaitClose {
            Log.d("Firestore", "Cerrando listener para: $categoria")
            listener.remove()
        }
    }

    fun getTodosLosProductosEnTiempoReal(): Flow<List<Producto>> {
        val verdurasFlow = getProductosPorCategoria("verduras")
        val frutasFlow = getProductosPorCategoria("frutas")

        return combine(verdurasFlow, frutasFlow) { verduras, frutas ->
            Log.d("Firestore", "Combinando ${verduras.size} verduras y ${frutas.size} frutas.")
            verduras + frutas
        }
    }
}
