// Archivo: VendorScreens.kt
// Este archivo reemplaza las implementaciones simples que tenías
package mx.edu.utng.proyectotacho

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

// ============================================
// EDITAR PERFIL
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(onBack: () -> Unit) {
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

            Button(
                onClick = { showSavedSnackbar = true },
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

// ============================================
// GESTIONAR PRODUCTOS
// ============================================
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    val isAvailable: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductsScreen(onBack: () -> Unit) {
    var products by remember {
        mutableStateOf(
            listOf(
                Product(1, "Pan Blanco", 15.0, 50, true),
                Product(2, "Pan Integral", 18.0, 30, true),
                Product(3, "Conchas", 12.0, 40, true),
                Product(4, "Bolillo", 3.0, 100, false),
                Product(5, "Pan Dulce", 15.0, 25, true)
            )
        )
    }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Productos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Outlined.Add, contentDescription = "Agregar producto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatsItem("Total", products.size.toString(), Icons.Outlined.Inventory)
                    StatsItem("Activos", products.count { it.isAvailable }.toString(), Icons.Outlined.CheckCircle)
                    StatsItem("Sin Stock", products.count { it.stock == 0 }.toString(), Icons.Outlined.Warning)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onToggleAvailability = {
                            products = products.map {
                                if (it.id == product.id) it.copy(isAvailable = !it.isAvailable)
                                else it
                            }
                        },
                        onDelete = {
                            products = products.filter { it.id != product.id }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Agregar Producto") },
            text = { Text("Funcionalidad de agregar producto próximamente") },
            confirmButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun StatsItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ProductCard(
    product: Product,
    onToggleAvailability: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (product.isAvailable)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Precio: $${product.price}", fontSize = 14.sp)
                Text(
                    "Stock: ${product.stock}",
                    fontSize = 12.sp,
                    color = if (product.stock > 10) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.error
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onToggleAvailability) {
                    Icon(
                        if (product.isAvailable) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "Cambiar disponibilidad",
                        tint = if (product.isAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de eliminar ${product.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// ============================================
// VER ESTADÍSTICAS
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewStatisticsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas del Negocio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
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
            var selectedPeriod by remember { mutableStateOf("Esta Semana") }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Período:", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hoy", "Esta Semana", "Este Mes").forEach { period ->
                            FilterChip(
                                selected = selectedPeriod == period,
                                onClick = { selectedPeriod = period },
                                label = { Text(period) }
                            )
                        }
                    }
                }
            }

            Text("Resumen General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Visitas", "128", "+12%", Icons.Outlined.Visibility, Blue500, Modifier.weight(1f))
                StatCard("Favoritos", "45", "+8%", Icons.Outlined.Favorite, Purple500, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Llamadas", "23", "+15%", Icons.Outlined.Phone, Green600, Modifier.weight(1f))
                StatCard("Direcciones", "67", "+5%", Icons.Outlined.LocationOn, Yellow500, Modifier.weight(1f))
            }

            Text("Productos Más Vistos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProductStatRow("Pan Blanco", 45, Blue500)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProductStatRow("Conchas", 38, Purple500)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProductStatRow("Pan Integral", 29, Green600)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProductStatRow("Pan Dulce", 24, Yellow500)
                }
            }

            Text("Horarios con Más Tráfico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TimeSlotBar("8:00 - 10:00 AM", 0.85f)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeSlotBar("10:00 - 12:00 PM", 0.65f)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeSlotBar("12:00 - 2:00 PM", 0.40f)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeSlotBar("2:00 - 4:00 PM", 0.55f)
                    Spacer(modifier = Modifier.height(8.dp))
                    TimeSlotBar("4:00 - 6:00 PM", 0.75f)
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    change: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(change, fontSize = 12.sp, color = Green600)
        }
    }
}

@Composable
fun ProductStatRow(name: String, views: Int, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Visibility, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("$views", color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun TimeSlotBar(time: String, percentage: Float) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(time, fontSize = 14.sp)
            Text("${(percentage * 100).toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}


