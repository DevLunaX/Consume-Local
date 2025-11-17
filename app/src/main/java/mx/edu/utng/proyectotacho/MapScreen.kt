import androidx.compose.foundation.Image
import androidx.compose.foundation.background
// ... Otras importaciones
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// **********************************************
// Importaciones requeridas para Google Maps Compose
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
// **********************************************

import mx.edu.utng.proyectotacho.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBusinessClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    // 1. Definir la posición inicial del mapa (ej. Ciudad de México)
    val cdMx = LatLng(19.4326, -99.1332)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cdMx, 13f) // Zoom 13 para nivel de ciudad
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // ... (Tu OutlinedTextField para la búsqueda)
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Busca tortillerías, fruterías...") },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = CircleShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Gray100,
                            unfocusedBorderColor = Gray100
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(currentScreen = Screen.Map, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // 2. 🚀 Reemplazo del Mapa Simulado por GoogleMap 🚀
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {

                // Usa el composable de Google Maps
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { /* Manejar clic en el mapa si es necesario */ }
                ) {
                    // Agregar un marcador (simulando un negocio)
                    Marker(
                        state = MarkerState(position = LatLng(19.4326, -99.1332 + 0.005)), // Posición ligeramente diferente
                        title = "Panadería \"El Buen Pan\"",
                        snippet = "El mejor pan de la zona"
                    )
                }

                FloatingActionButton(
                    onClick = { /* Lógica para centrar en la ubicación actual del usuario */ },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                ) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = "Ubicarme")
                }
            }
            // Lista de negocios
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(16.dp)
            ) {
                Text("Negocios cerca de ti", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { BusinessListItem("Panadería \"El Buen Pan\"", "★ 4.8 • Panadería • 200m", "https://i.imgur.com/GscB5hM.jpeg", onBusinessClick) }
                    item { BusinessListItem("Frutería \"La Huerta\"", "★ 4.9 • Frutas y Verduras • 350m", "https://i.imgur.com/u5u06xV.jpeg", onBusinessClick) }
                }
            }
        }
    }
}

@Composable
fun BusinessListItem(name: String, details: String, imageUrl: String, onClick: () -> Unit) {
    // ... (Tu BusinessListItem composable sin cambios)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(name, fontWeight = FontWeight.Bold)
            Text(details, color = Gray500, fontSize = 14.sp)
        }
    }
}