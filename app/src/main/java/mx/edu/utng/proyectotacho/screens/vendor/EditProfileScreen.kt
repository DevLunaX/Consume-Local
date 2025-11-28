package mx.edu.utng.proyectotacho.screens.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.UsuarioApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVendorProfileScreen( // <--- NOMBRE CORREGIDO PARA EVITAR CONFLICTOS
    onBack: () -> Unit
) {
    // === LÓGICA FIREBASE ===
    val authService = remember { AuthService() }
    val scope = rememberCoroutineScope()
    var currentUser by remember { mutableStateOf<UsuarioApp?>(null) }

    // Estados de carga
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // Campos del formulario
    var businessName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var openingHours by remember { mutableStateOf("") }

    var showSavedSnackbar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // 1. CARGAR DATOS AL INICIAR
    LaunchedEffect(Unit) {
        val resultado = authService.obtenerUsuarioActual()
        resultado.onSuccess { user ->
            currentUser = user
            businessName = user.nombreNegocio ?: ""
            description = user.descripcion ?: ""
            phone = user.telefono
            address = user.direccion ?: ""
            isLoading = false
        }
        resultado.onFailure {
            errorMessage = "Error al cargar datos"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil del Negocio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = {
            if (showSavedSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    action = {
                        TextButton(onClick = { showSavedSnackbar = false }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text("✓ Cambios guardados exitosamente")
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Información básica
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Información Básica", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("Nombre del Negocio") },
                            leadingIcon = { Icon(Icons.Outlined.Store, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descripción") },
                            leadingIcon = { Icon(Icons.Outlined.Description, null) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                }

                // Información de contacto
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Teléfono") },
                            leadingIcon = { Icon(Icons.Outlined.Phone, null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Dirección") },
                            leadingIcon = { Icon(Icons.Outlined.LocationOn, null) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }
                }

                // Horario
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Horario de Atención", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = openingHours,
                            onValueChange = { openingHours = it },
                            label = { Text("Horario (Ej. Lun-Vie 9-6)") },
                            leadingIcon = { Icon(Icons.Outlined.Schedule, null) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }

                // Botón de guardar
                Button(
                    onClick = {
                        if (currentUser != null) {
                            isSaving = true
                            scope.launch {
                                val usuarioActualizado = currentUser!!.copy(
                                    nombreNegocio = businessName,
                                    descripcion = description,
                                    telefono = phone,
                                    direccion = address
                                )
                                val resultado = authService.actualizarUsuario(usuarioActualizado)
                                isSaving = false
                                if (resultado.isSuccess) {
                                    showSavedSnackbar = true
                                } else {
                                    errorMessage = "Error al guardar cambios"
                                }
                            }
                        }
                    },
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Outlined.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Cambios")
                    }
                }
            }
        }
    }
}