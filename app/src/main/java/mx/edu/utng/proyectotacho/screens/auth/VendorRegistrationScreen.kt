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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

import kotlinx.coroutines.launch // <--- IMPORTANTE
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.UsuarioApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorRegistrationScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // Estados del formulario (Igual que antes)
    var nombreNegocio by remember { mutableStateOf("") }
    var nombrePropietario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipoNegocio by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val tiposNegocio = listOf("Restaurante", "Cafetería", "Panadería", "Tienda de Abarrotes", "Ferretería", "Ropa y Accesorios", "Servicios", "Otro")

    // === LÓGICA FIREBASE ===
    val scope = rememberCoroutineScope()
    val authService = remember { AuthService() }
    var isLoading by remember { mutableStateOf(false) }
    // =======================

    Surface(color = Amber50, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Registro de Vendedor") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Gray600, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (Toda tu UI de Iconos y TextFields se queda IGUAL hasta llegar al botón) ...
                // POR BREVEDAD, ASUMO QUE AQUÍ ESTÁN TODOS TUS TEXTFIELDS (Nombre, Email, Dropdown, etc.)

                // NOTA: Asegúrate de copiar tus TextFields de vuelta aquí si borraste algo.
                // ...
                Icon(Icons.Default.ShoppingCart, null, Modifier.size(80.dp), tint = Gray700)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Registra tu negocio", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Gray500)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(value = nombreNegocio, onValueChange = { nombreNegocio = it }, label = { Text("Nombre del Negocio") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = nombrePropietario, onValueChange = { nombrePropietario = it }, label = { Text("Nombre del Propietario") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown (Simplificado para el ejemplo, usa tu código original del Dropdown)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(value = tipoNegocio, onValueChange = {}, readOnly = true, label = { Text("Tipo de Negocio") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        tiposNegocio.forEach { tipo -> DropdownMenuItem(text = { Text(tipo) }, onClick = { tipoNegocio = tipo; expanded = false }) }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(120.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700))


                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // === BOTÓN REGISTRO CON LÓGICA ===
                Button(
                    enabled = !isLoading,
                    onClick = {
                        when {
                            // Validaciones
                            nombreNegocio.isBlank() || nombrePropietario.isBlank() || email.isBlank() ||
                                    telefono.isBlank() || direccion.isBlank() || tipoNegocio.isBlank() ||
                                    password.isBlank() || confirmPassword.isBlank() -> {
                                showError = true
                                errorMessage = "Todos los campos son obligatorios"
                            }
                            password != confirmPassword -> {
                                showError = true
                                errorMessage = "Las contraseñas no coinciden"
                            }
                            password.length < 6 -> {
                                showError = true
                                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                            }
                            else -> {
                                showError = false
                                isLoading = true

                                scope.launch {
                                    // CREAMOS EL OBJETO CON DATOS DE VENDEDOR
                                    val nuevoVendedor = UsuarioApp(
                                        email = email,
                                        nombre = nombrePropietario,
                                        rol = "VENDEDOR", // <--- CLAVE PARA EL ROL
                                        telefono = telefono,
                                        nombreNegocio = nombreNegocio,
                                        categoria = tipoNegocio,
                                        direccion = direccion,
                                        descripcion = descripcion
                                    )

                                    val resultado = authService.registrarUsuario(email, password, nuevoVendedor)
                                    isLoading = false

                                    if (resultado.isSuccess) {
                                        onRegisterSuccess()
                                    } else {
                                        showError = true
                                        errorMessage = resultado.exceptionOrNull()?.localizedMessage ?: "Error al registrar"
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray600),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Registrar Negocio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onBack) { Text("¿Ya tienes cuenta? Inicia sesión", color = Gray700) }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}