import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.components.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBusinessClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estados
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showBusinessList by remember { mutableStateOf(false) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Ubicación inicial (Dolores Hidalgo, Guanajuato)
    val initialLocation = LatLng(21.1561, -100.9319)
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }

    // Configuración del mapa
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                isTrafficEnabled = false
            )
        )
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true,
                mapToolbarEnabled = false
            )
        )
    }

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            getCurrentLocation(context) { location ->
                currentLocation = location
                scope.launch {
                    cameraPositionState.animate(
                        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, 17f),
                        durationMs = 1000
                    )
                }
            }
        }
    }

    // Lista de negocios de ejemplo
    val businesses = remember {
        listOf(
            Business("Panadería \"El Buen Pan\"", "★ 4.8 • Panadería • 200m", "https://i.imgur.com/GscB5hM.jpeg", LatLng(21.1571, -100.9329)),
            Business("Frutería \"La Huerta\"", "★ 4.9 • Frutas y Verduras • 350m", "https://i.imgur.com/u5u06xV.jpeg", LatLng(21.1551, -100.9309)),
            Business("Tortillería \"Don José\"", "★ 4.7 • Tortillería • 150m", "https://i.imgur.com/GscB5hM.jpeg", LatLng(21.1581, -100.9339)),
            Business("Carnicería \"La Especial\"", "★ 4.6 • Carnicería • 400m", "https://i.imgur.com/u5u06xV.jpeg", LatLng(21.1541, -100.9299))
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa de Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapClick = {
                isSearchActive = false
                showBusinessList = false
            }
        ) {
            // Marcadores de negocios
            businesses.forEach { business ->
                Marker(
                    state = MarkerState(position = business.location),
                    title = business.name,
                    snippet = business.details,
                    onClick = {
                        showBusinessList = true
                        false
                    }
                )
            }
        }

        // Barra de búsqueda flotante
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = Gray600,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar negocios...", color = Gray400) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        IconButton(onClick = {
                            isSearchActive = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Gray600)
                        }
                    } else {
                        Text(
                            text = "Buscar negocios cercanos...",
                            color = Gray500,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isSearchActive = true }
                        )

                        IconButton(onClick = { /* Abrir filtros */ }) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = "Filtros",
                                tint = Gray600
                            )
                        }
                    }
                }
            }
        }

        // Botones flotantes en la esquina inferior derecha
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón para ver lista de negocios
            FloatingActionButton(
                onClick = { showBusinessList = !showBusinessList },
                containerColor = if (showBusinessList) Amber500 else Color.White,
                contentColor = if (showBusinessList) Color.White else Gray700,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FilterList,
                    contentDescription = "Ver lista"
                )
            }

            // Botón de ubicación actual
            FloatingActionButton(
                onClick = {
                    if (hasLocationPermission) {
                        getCurrentLocation(context) { location ->
                            currentLocation = location
                            scope.launch {
                                cameraPositionState.animate(
                                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, 17f),
                                    durationMs = 1000
                                )
                            }
                        }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                containerColor = Amber500,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Lista deslizable de negocios (desde abajo)
        AnimatedVisibility(
            visible = showBusinessList,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 80.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Barra superior
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Negocios cerca de ti",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Gray700
                        )

                        IconButton(onClick = { showBusinessList = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Gray600)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Lista de negocios
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(businesses.size) { index ->
                            val business = businesses[index]
                            BusinessListItem(
                                name = business.name,
                                details = business.details,
                                imageUrl = business.imageUrl,
                                onClick = {
                                    onBusinessClick()
                                    showBusinessList = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            AppBottomNavigationBar(
                currentScreen = Screen.Map,
                onNavigate = onNavigate
            )
        }
    }
}

// Función para obtener la ubicación actual
private fun getCurrentLocation(
    context: Context,
    onLocationReceived: (LatLng) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            }
        }
    } catch (e: SecurityException) {
        // Manejar excepción
    }
}

@Composable
fun BusinessListItem(
    name: String,
    details: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Gray50),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Gray700
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = details,
                    color = Gray600,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Data class para negocios
data class Business(
    val name: String,
    val details: String,
    val imageUrl: String,
    val location: LatLng
)