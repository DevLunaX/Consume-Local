// Archivo: EditProfileScreen.kt
package mx.edu.utng.proyectotacho

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdiProfileScreen(onBack: () -> Unit) {
    var businessName by remember { mutableStateOf("Panadería \"El Buen Pan\"") }
    var description by remember { mutableStateOf("Panadería artesanal con más de 20 años de tradición") }
    var phone by remember { mutableStateOf("473-123-4567") }
    var address by remember { mutableStateOf("Calle Hidalgo #45, Centro") }
    var openingHours by remember { mutableStateOf("Lun-Sáb: 7:00 AM - 8:00 PM") }
    var showSavedSnackbar by remember { mutableStateOf(false) }

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
                    action = {
                        TextButton(onClick = { showSavedSnackbar = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text("✓ Cambios guardados exitosamente")
                }
            }
        }
    ) { paddingValues ->
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
                    Text(
                        "Información Básica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Nombre del Negocio") },
                        leadingIcon = { Icon(Icons.Outlined.Store, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            // Información de contacto
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Contacto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Teléfono") },
                        leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Dirección") },
                        leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }

            // Horario
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Horario de Atención",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = openingHours,
                        onValueChange = { openingHours = it },
                        label = { Text("Horario") },
                        leadingIcon = { Icon(Icons.Outlined.Schedule, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }

            // Botón de guardar
            Button(
                onClick = {
                    showSavedSnackbar = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Outlined.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Cambios")
            }
        }
    }
}