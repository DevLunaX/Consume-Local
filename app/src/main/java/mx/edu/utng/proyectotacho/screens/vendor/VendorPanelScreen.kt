// Archivo: VendorPanelScreen.kt
package mx.edu.utng.proyectotacho.screens.vendor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorPanelScreen(
    // ¡Cambio clave! En lugar de NavController, pasamos funciones lambda.
    onExit: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToManageProducts: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Mi Negocio", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onExit) {
                        Text("Salir", color = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Panadería \"El Buen Pan\"", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Visitas al perfil esta semana: 128", color = Gray500, fontSize = 14.sp)
                }
            }

            // Mapeamos cada item a su respectiva función de navegación
            val panelItems = listOf(
                PanelItem("Editar Perfil", Icons.Outlined.Create, Blue500, onNavigateToEditProfile),
                PanelItem("Gestionar Productos", Icons.Outlined.Image, Purple500, onNavigateToManageProducts),
                PanelItem("Ver Estadísticas", Icons.Outlined.BarChart, Green600, onNavigateToStatistics),
                PanelItem("Ver Reseñas", Icons.Outlined.Chat, Yellow500, onNavigateToReviews)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(panelItems) { item ->
                    PanelCard(item = item)
                }
            }
        }
    }
}

// Actualizamos la data class para que contenga la acción (la función lambda)
data class PanelItem(val title: String, val icon: ImageVector, val color: Color, val action: () -> Unit)

@Composable
fun PanelCard(item: PanelItem) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = item.action), // ¡Aquí está la conexión!
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(item.icon, contentDescription = item.title, tint = item.color, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.title, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        }
    }
}