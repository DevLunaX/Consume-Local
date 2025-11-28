import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.R
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.components.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.ui.theme.*
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.UsuarioApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBusinessClick: (UsuarioApp) -> Unit, // Recibe el objeto negocio
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authService = remember { AuthService() }

    // Datos en tiempo real
    var listaNegociosReales by remember { mutableStateOf<List<UsuarioApp>>(emptyList()) }

    // === ESTADO CLAVE: NEGOCIO SELECCIONADO ===
    var selectedBusiness by remember { mutableStateOf<UsuarioApp?>(null) }
    // ==========================================

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Cargar negocios
    LaunchedEffect(Unit) {
        authService.escucharNegociosEnTiempoReal { negocios ->
            listaNegociosReales = negocios
        }
    }

    val initialLocation = LatLng(21.1561, -100.9319)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 14f)
    }

    val mapProperties by remember(hasLocationPermission) {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                isTrafficEnabled = false,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            getCurrentLocation(context) { location ->
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(location, 16f))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. EL MAPA
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = true),
            onMapClick = {
                // Si tocan el mapa vacío, cerramos la tarjeta
                selectedBusiness = null
                isSearchActive = false
            }
        ) {
            listaNegociosReales.forEach { negocio ->
                Marker(
                    state = MarkerState(position = LatLng(negocio.latitud, negocio.longitud)),
                    title = negocio.nombreNegocio,
                    onClick = {
                        // AL TOCAR UN PIN, SELECCIONAMOS ESE NEGOCIO
                        selectedBusiness = negocio
                        false // false = centrar cámara y abrir info window default (opcional)
                    }
                )
            }
        }

        // 2. BARRA DE BÚSQUEDA (Igual que antes)
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Search, "Buscar", tint = Gray600, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery, onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar...", color = Gray400) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent),
                            singleLine = true
                        )
                        IconButton(onClick = { isSearchActive = false; searchQuery = "" }) { Icon(Icons.Default.Close, "Cerrar", tint = Gray600) }
                    } else {
                        Text("Buscar negocios cercanos...", color = Gray500, modifier = Modifier.weight(1f).clickable { isSearchActive = true })
                    }
                }
            }
        }

        // 3. BOTÓN DE UBICACIÓN (Lo moví un poco para que no tape la tarjeta)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 180.dp, end = 16.dp) // Subido para no estorbar
        ) {
            FloatingActionButton(
                onClick = {
                    if (hasLocationPermission) {
                        getCurrentLocation(context) { loc ->
                            scope.launch { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(loc, 17f)) }
                        }
                    } else { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                },
                containerColor = Amber500,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.MyLocation, "Mi ubicación")
            }
        }

        // 4. TARJETA FLOTANTE DEL NEGOCIO SELECCIONADO
        // Solo aparece si selectedBusiness NO es nulo
        AnimatedVisibility(
            visible = selectedBusiness != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            if (selectedBusiness != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 90.dp) // Espacio para el BottomBar
                        .clickable { onBusinessClick(selectedBusiness!!) }, // AL CLICK, NAVEGAMOS
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono del negocio
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Amber100, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Store, null, tint = Amber600, modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedBusiness!!.nombreNegocio ?: "Negocio",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Gray900
                            )
                            Text(
                                text = selectedBusiness!!.categoria ?: "Comercio Local",
                                color = Gray500,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Ver detalles >",
                                color = Blue500,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Botón cerrar
                        IconButton(onClick = { selectedBusiness = null }) {
                            Icon(Icons.Default.Close, "Cerrar", tint = Gray400)
                        }
                    }
                }
            }
        }

        // 5. BARRA DE NAVEGACIÓN INFERIOR
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            AppBottomNavigationBar(currentScreen = Screen.Map, onNavigate = onNavigate)
        }
    }
}

private fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    try {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
            location?.let { onLocationReceived(LatLng(it.latitude, it.longitude)) }
        }
    } catch (e: SecurityException) { }
}