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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

// ==================== INICIO DE SESIÓN PARA VENDEDORES ====================
import kotlinx.coroutines.launch // <--- NO OLVIDES ESTE IMPORT
import mx.edu.utng.proyectotacho.AuthService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorLoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: (String) -> Unit, // <--- CAMBIO: Recibe el rol (String)
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // === LÓGICA FIREBASE ===
    val scope = rememberCoroutineScope()
    val authService = remember { AuthService() }
    var isLoading by remember { mutableStateOf(false) }
    // =======================

    Surface(
        color = Amber50,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = { Text("Panel de Vendedor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Gray600,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // ... (Iconos y textos se mantienen igual) ...
                Icon(Icons.Default.ShoppingCart, null, Modifier.size(100.dp), tint = Gray700)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Acceso Vendedor", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Gray700)
                Text("Administra tu negocio", fontSize = 16.sp, color = Gray700, modifier = Modifier.padding(top = 8.dp))
                Spacer(modifier = Modifier.height(32.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Warning else Icons.Default.Lock, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gray600, focusedLabelColor = Gray700)
                )

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 14.sp, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Olvidé contraseña... (Igual)
                TextButton(onClick = { /* TODO */ }, modifier = Modifier.align(Alignment.End)) {
                    Text("¿Olvidaste tu contraseña?", color = Gray700, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // === BOTÓN LOGIN CON LÓGICA ===
                Button(
                    enabled = !isLoading,
                    onClick = {
                        when {
                            email.isBlank() || password.isBlank() -> {
                                showError = true
                                errorMessage = "Por favor completa todos los campos"
                            }
                            else -> {
                                showError = false
                                isLoading = true

                                scope.launch {
                                    val resultado = authService.login(email, password)
                                    isLoading = false

                                    resultado.onSuccess { rol ->
                                        // Devolvemos el rol hacia el MainActivity
                                        onLoginSuccess(rol)
                                    }
                                    resultado.onFailure {
                                        showError = true
                                        errorMessage = "Error: Verifica tus credenciales."
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
                        Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Registro
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("¿No tienes cuenta?", color = Gray700)
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Registra tu negocio", color = Gray600, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}