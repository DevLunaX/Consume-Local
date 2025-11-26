package mx.edu.utng.proyectotacho

// 1. LOS IMPORTS SIEMPRE VAN ARRIBA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// 2. MODELO DE DATOS
// Nota: Es vital que todos los campos tengan un valor por defecto (="" o =null)
// para que Firebase pueda convertir el documento a este objeto automáticamente.
// En AuthService.kt
data class UsuarioApp(
    val id: String = "",
    val email: String = "",
    val rol: String = "",
    val nombre: String = "", // Nombre del propietario
    val username: String = "", //user name
    val telefono: String = "", // <--- AGREGADO
    // Para vendedores:
    val nombreNegocio: String? = null,
    val categoria: String? = null, // Tipo de negocio
    val direccion: String? = null, // <--- AGREGADO
    val descripcion: String? = null // <--- AGREGADO
)

// 3. LA CLASE DE SERVICIO
class AuthService {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // FUNCION PARA REGISTRAR-------------------------1-
    suspend fun registrarUsuario(
        email: String,
        pass: String,
        datosUsuario: UsuarioApp
    ): Result<String> {
        return try {
            // A. Crear usuario en Auth
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al obtener ID")

            // B. Guardar en Firestore con el ID real
            val usuarioAGuardar = datosUsuario.copy(id = userId)

            // Usamos .set() para crear el documento con el mismo ID del usuario
            db.collection("users").document(userId).set(usuarioAGuardar).await()

            Result.success(userId)
        } catch (e: Exception) {
            // Si falla, devolvemos el error para mostrarlo en pantalla
            Result.failure(e)
        }
    }

    // FUNCION PARA LOGIN----------------------------------------2-----------------
    suspend fun login(email: String, pass: String): Result<String> {
        return try {
            // A. Login
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val userId = authResult.user?.uid ?: throw Exception("ID nulo")

            // B. Pedir el ROL a la base de datos
            val document = db.collection("users").document(userId).get().await()

            // Si por error no tiene rol, asumimos que es CLIENTE para que no truene
            val rol = document.getString("rol") ?: "CLIENTE"

            Result.success(rol)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // 3. FUNCION PARA OBTENER LOS DATOS DEL USUARIO ACTUAL----------------------------3-----------------------
    suspend fun obtenerUsuarioActual(): Result<UsuarioApp> {
        return try {
            // 1. Obtenemos el ID del usuario que tiene sesión iniciada
            val userId = auth.currentUser?.uid ?: throw Exception("No hay sesión iniciada")

            // 2. Buscamos su documento en la base de datos
            val document = db.collection("users").document(userId).get().await()

            // 3. Convertimos ese documento a tu objeto UsuarioApp
            val usuario = document.toObject(UsuarioApp::class.java)
                ?: throw Exception("No se encontraron datos")

            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 4. FUNCION PARA ACTUALIZAR DATOS DEL USUARIO-----------------------------4------------------------------
    suspend fun actualizarUsuario(usuarioActualizado: UsuarioApp): Result<Unit> {
        return try {
            val userId = usuarioActualizado.id
            if (userId.isEmpty()) throw Exception("ID de usuario no válido")

            // Sobrescribimos el documento con los nuevos datos
            db.collection("users").document(userId).set(usuarioActualizado).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Función para cerrar sesión
    fun cerrarSesion() {
        auth.signOut()
    }
}