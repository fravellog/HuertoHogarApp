package com.example.huertohogartiendaapp.data // O el paquete donde lo crees

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Usamos un 'object' para que sea un singleton,
// es decir, una única instancia para toda la app.
object FirebaseManager {
    // Instancia para manejar la autenticación
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Instancia para manejar la base de datos
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}
