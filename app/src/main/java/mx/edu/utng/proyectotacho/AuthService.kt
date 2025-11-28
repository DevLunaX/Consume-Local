package mx.edu.utng.proyectotacho

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldValue

// 2. MODELO DE DATOS
data class UsuarioApp(
    val id: String = "",
    val email: String = "",
    val rol: String = "",
    val nombre: String = "",
    val username: String = "",
    val telefono: String = "",
    val nombreNegocio: String? = null,
    val categoria: String? = null,
    val direccion: String? = null,
    val descripcion: String? = null,
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    // Estadísticas
    val visitas: Int = 0,
    val ratingPromedio: Double = 0.0,
    val totalResenas: Int = 0
)

// 3. LA CLASE DE SERVICIO
class AuthService {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // 1. REGISTRAR
    suspend fun registrarUsuario(email: String, pass: String, datosUsuario: UsuarioApp): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al obtener ID")
            val usuarioAGuardar = datosUsuario.copy(id = userId)
            db.collection("users").document(userId).set(usuarioAGuardar).await()
            Result.success(userId)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 2. LOGIN
    suspend fun login(email: String, pass: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("ID nulo")
            val document = db.collection("users").document(userId).get().await()
            val rol = document.getString("rol") ?: "CLIENTE"
            Result.success(rol)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 3. OBTENER USUARIO ACTUAL
    suspend fun obtenerUsuarioActual(): Result<UsuarioApp> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No hay sesión")
            val document = db.collection("users").document(userId).get().await()
            val usuario = document.toObject(UsuarioApp::class.java) ?: throw Exception("Sin datos")
            Result.success(usuario)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 4. ACTUALIZAR USUARIO
    suspend fun actualizarUsuario(usuarioActualizado: UsuarioApp): Result<Unit> {
        return try {
            val userId = usuarioActualizado.id
            if (userId.isEmpty()) throw Exception("ID inválido")
            db.collection("users").document(userId).set(usuarioActualizado).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 5. ESCUCHAR NEGOCIOS (MAPA)
    fun escucharNegociosEnTiempoReal(onDatosActualizados: (List<UsuarioApp>) -> Unit) {
        db.collection("users")
            .whereEqualTo("rol", "VENDEDOR")
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener
                if (snapshots != null) {
                    val lista = snapshots.toObjects(UsuarioApp::class.java)
                    // Filtramos negocios válidos
                    val validos = lista.filter { it.latitud != 0.0 && it.longitud != 0.0 && !it.nombreNegocio.isNullOrBlank() }
                    onDatosActualizados(validos)
                }
            }
    }

    // 6. AGREGAR FAVORITO
    suspend fun agregarFavorito(negocio: UsuarioApp): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No logueado")
            db.collection("users").document(userId).collection("favorites").document(negocio.id).set(negocio).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 7. ELIMINAR FAVORITO
    suspend fun eliminarFavorito(negocioId: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No logueado")
            db.collection("users").document(userId).collection("favorites").document(negocioId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // 8. VERIFICAR FAVORITO
    suspend fun esFavorito(negocioId: String): Boolean {
        return try {
            val userId = auth.currentUser?.uid ?: return false
            val doc = db.collection("users").document(userId).collection("favorites").document(negocioId).get().await()
            doc.exists()
        } catch (e: Exception) { false }
    }

    // 9. OBTENER FAVORITOS
    suspend fun obtenerFavoritos(): Result<List<UsuarioApp>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No logueado")
            val snapshot = db.collection("users").document(userId).collection("favorites").get().await()
            Result.success(snapshot.toObjects(UsuarioApp::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }

    // ====================================================================================
    // AQUI ESTABA EL ERROR: FALTABA LLAMAR A recalcularPromedio
    // ====================================================================================

    // 10. SUBIR RESEÑA
    suspend fun subirResena(review: Review): Result<Unit> {
        return try {
            val docRef = db.collection("reviews").document()
            val reviewConId = review.copy(id = docRef.id)
            docRef.set(reviewConId).await()

            // ¡ESTO ES LO QUE FALTABA!
            // Avisamos que hay reseña nueva para actualizar las estrellas del negocio
            recalcularPromedio(review.businessId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 11. ESCUCHAR RESEÑAS (TIEMPO REAL)
    fun escucharResenas(businessId: String, onReviewsUpdate: (List<Review>) -> Unit) {
        db.collection("reviews")
            .whereEqualTo("businessId", businessId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener
                if (snapshots != null) {
                    onReviewsUpdate(snapshots.toObjects(Review::class.java))
                }
            }
    }

    // 12-15. ESTADÍSTICAS PERFIL USUARIO
    suspend fun contarFavoritos(): Int {
        return try {
            val userId = auth.currentUser?.uid ?: return 0
            val snapshot = db.collection("users").document(userId).collection("favorites").get().await()
            snapshot.size()
        } catch (e: Exception) { 0 }
    }

    suspend fun contarMisResenas(): Int {
        return try {
            val userId = auth.currentUser?.uid ?: return 0
            val snapshot = db.collection("reviews").whereEqualTo("userId", userId).get().await()
            snapshot.size()
        } catch (e: Exception) { 0 }
    }

    suspend fun contarVisitas(): Int {
        return try {
            val userId = auth.currentUser?.uid ?: return 0
            val snapshot = db.collection("users").document(userId).collection("visits").get().await()
            snapshot.size()
        } catch (e: Exception) { 0 }
    }

    suspend fun registrarVisita(businessId: String) {
        try {
            val userId = auth.currentUser?.uid ?: return
            val visitaData = hashMapOf("businessId" to businessId, "timestamp" to System.currentTimeMillis())
            db.collection("users").document(userId).collection("visits").document(businessId).set(visitaData).await()
        } catch (e: Exception) { }
    }

    // 16. OBTENER MIS RESEÑAS
    suspend fun obtenerMisResenasList(): Result<List<Review>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No logueado")
            val snapshot = db.collection("reviews").whereEqualTo("userId", userId).get().await()
            Result.success(snapshot.toObjects(Review::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }

    // 17. OBTENER MIS VISITAS
    suspend fun obtenerMisVisitasList(): Result<List<UsuarioApp>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("No logueado")
            val visitsSnapshot = db.collection("users").document(userId).collection("visits").get().await()
            val businessIds = visitsSnapshot.documents.map { it.id }
            if (businessIds.isEmpty()) return Result.success(emptyList())

            // Firestore 'in' soporta max 10, para escolar está bien.
            val businessesSnapshot = db.collection("users").whereIn("id", businessIds.take(10)).get().await()
            Result.success(businessesSnapshot.toObjects(UsuarioApp::class.java))
        } catch (e: Exception) { Result.failure(e) }
    }

    // 18. INCREMENTAR VISITAS (NEGOCIO)
    suspend fun incrementarVisitas(businessId: String) {
        try {
            db.collection("users").document(businessId)
                .update("visitas", FieldValue.increment(1))
                .await()
        } catch (e: Exception) { }
    }

    // 19. RECALCULAR PROMEDIO (NEGOCIO)
    suspend fun recalcularPromedio(businessId: String) {
        try {
            // 1. Bajamos TODAS las reseñas de ese negocio
            val snapshot = db.collection("reviews")
                .whereEqualTo("businessId", businessId)
                .get().await()

            val reviews = snapshot.toObjects(Review::class.java)

            if (reviews.isNotEmpty()) {
                // 2. Calculamos el nuevo promedio matemático
                val totalEstrellas = reviews.sumOf { it.rating }
                val totalVotos = reviews.size
                // Ejemplo: (5 + 4 + 5) / 3 = 4.66
                val nuevoPromedio = totalEstrellas.toDouble() / totalVotos

                // 3. Guardamos el nuevo rating en el perfil del negocio
                db.collection("users").document(businessId)
                    .update(mapOf(
                        "ratingPromedio" to nuevoPromedio,
                        "totalResenas" to totalVotos
                    )).await()
            }
        } catch (e: Exception) { }
    }

    // ESCUCHAR MI PROPIO NEGOCIO (PANEL VENDEDOR)
    fun escucharMiNegocio(onDatosActualizados: (UsuarioApp) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    val usuario = snapshot.toObject(UsuarioApp::class.java)
                    if (usuario != null) {
                        onDatosActualizados(usuario)
                    }
                }
            }
    }

    fun cerrarSesion() {
        auth.signOut()
    }
}

// DATA CLASS DE REVIEW
data class Review(
    val id: String = "",
    val businessId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)