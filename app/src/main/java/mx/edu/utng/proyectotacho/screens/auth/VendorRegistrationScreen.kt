package mx.edu.utng.proyectotacho.screens.vendor // O tu paquete correspondiente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.UsuarioApp
import mx.edu.utng.proyectotacho.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorRegistrationScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // Variables del formulario
    var nombreNegocio by remember { mutableStateOf("") }
    var nombrePropietario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipoNegocio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Variables visuales
    var expanded by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // === VARIABLES PARA EL MAPA ===
    var showMapDialog by remember { mutableStateOf(false) } // Controla si se ve el mapa
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // Guarda la coordenada
    // ==============================

    val tiposNegocio = listOf("Restaurante", "Cafetería", "Panadería", "Tienda", "Servicios", "Otro")
    val scope = rememberCoroutineScope()
    val authService = remember { AuthService() }
    var isLoading by remember { mutableStateOf(false) }

    Surface(color = Amber50, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = { Text("Registro de Negocio") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Gray600, titleContentColor = Color.White)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Store, null, Modifier.size(80.dp), tint = Gray700)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Datos del Negocio", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Gray700)
                Spacer(modifier = Modifier.height(24.dp))

                // Campos normales...
                OutlinedTextField(value = nombreNegocio, onValueChange = { nombreNegocio = it }, label = { Text("Nombre del Negocio") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = nombrePropietario, onValueChange = { nombrePropietario = it }, label = { Text("Nombre del Propietario") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))

                // Dirección (Texto)
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección (Calle y Número)") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                // === BOTÓN PARA ABRIR EL MAPA ===
                OutlinedButton(
                    onClick = { showMapDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if(selectedLocation != null) Green600.copy(alpha = 0.1f) else Color.Transparent
                    )
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedLocation != null) "✓ Ubicación Seleccionada" else "Seleccionar Ubicación en Mapa")
                }
                if(selectedLocation != null){
                    Text("Coordenadas: ${selectedLocation!!.latitude.toString().take(7)}, ${selectedLocation!!.longitude.toString().take(7)}", fontSize = 12.sp, color = Gray600)
                }
                // ================================

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Tipo Negocio
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = tipoNegocio, onValueChange = {}, readOnly = true, label = { Text("Tipo de Negocio") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        tiposNegocio.forEach { tipo -> DropdownMenuItem(text = { Text(tipo) }, onClick = { tipoNegocio = tipo; expanded = false }) }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp))

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Registro
                Button(
                    onClick = {
                        when {
                            nombreNegocio.isBlank() || email.isBlank() || password.isBlank() -> {
                                showError = true
                                errorMessage = "Faltan datos obligatorios"
                            }
                            password != confirmPassword -> {
                                showError = true
                                errorMessage = "Las contraseñas no coinciden"
                            }
                            // Validamos que haya seleccionado ubicación
                            selectedLocation == null -> {
                                showError = true
                                errorMessage = "Por favor selecciona la ubicación en el mapa"
                            }
                            else -> {
                                showError = false
                                isLoading = true
                                scope.launch {
                                    val nuevoVendedor = UsuarioApp(
                                        email = email,
                                        nombre = nombrePropietario,
                                        rol = "VENDEDOR",
                                        telefono = telefono,
                                        nombreNegocio = nombreNegocio,
                                        categoria = tipoNegocio,
                                        direccion = direccion,
                                        descripcion = descripcion,
                                        // === GUARDAMOS LAS COORDENADAS ===
                                        latitud = selectedLocation!!.latitude,
                                        longitud = selectedLocation!!.longitude
                                    )
                                    val resultado = authService.registrarUsuario(email, password, nuevoVendedor)
                                    isLoading = false
                                    if (resultado.isSuccess) onRegisterSuccess()
                                    else {
                                        showError = true
                                        errorMessage = "Error al registrar"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray600)
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White) else Text("Registrar Negocio")
                }
            }
        }
    }

    // === VENTANA EMERGENTE DEL MAPA (DIALOG) ===
    if (showMapDialog) {
        Dialog(onDismissRequest = { showMapDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            // Centrado en Dolores Hidalgo por defecto
                            position = CameraPosition.fromLatLngZoom(LatLng(21.1561, -100.9319), 14f)
                        },
                        uiSettings = MapUiSettings(zoomControlsEnabled = true),
                        onMapClick = { latLng ->
                            // AL HACER CLIC EN EL MAPA, GUARDAMOS ESA UBICACIÓN
                            selectedLocation = latLng
                        }
                    ) {
                        // Si ya hay una ubicación seleccionada, mostramos el marcador ahí
                        if (selectedLocation != null) {
                            Marker(
                                state = MarkerState(position = selectedLocation!!),
                                title = "Ubicación de tu negocio"
                            )
                        }
                    }

                    // Botón para confirmar
                    Button(
                        onClick = { showMapDialog = false },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                    ) {
                        Text(if(selectedLocation == null) "Toca el mapa para seleccionar" else "Confirmar Ubicación")
                    }
                }
            }
        }
    }
}