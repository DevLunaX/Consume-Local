package mx.edu.utng.proyectotacho.screens.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.UsuarioApp
import mx.edu.utng.proyectotacho.components.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.ui.theme.*

@Composable
fun FavoritesScreen(
    // IMPORTANTE: Cambié esto para que reciba el objeto negocio al dar clic
    onBusinessClick: (UsuarioApp) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val authService = remember { AuthService() }
    val scope = rememberCoroutineScope()

    // Lista real de favoritos
    var favoritesList by remember { mutableStateOf<List<UsuarioApp>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar favoritos al entrar
    LaunchedEffect(Unit) {
        val resultado = authService.obtenerFavoritos()
        resultado.onSuccess {
            favoritesList = it
            isLoading = false
        }
        resultado.onFailure { isLoading = false }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header (Tu diseño original)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.verticalGradient(listOf(Pink500, Pink600)))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        IconButton(onClick = { onNavigate(Screen.Map) }, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Column {
                            Text("Mis Favoritos", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${favoritesList.size} negocios", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            // Contenido
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Pink500)
                }
            } else if (favoritesList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritesList) { negocio ->
                        ModernBusinessCard(
                            business = negocio,
                            onClick = { onBusinessClick(negocio) },
                            onRemoveFavorite = {
                                // Lógica para borrar de la lista visual y de la BD
                                scope.launch {
                                    authService.eliminarFavorito(negocio.id)
                                    // Recargamos la lista
                                    val res = authService.obtenerFavoritos()
                                    if(res.isSuccess) favoritesList = res.getOrNull() ?: emptyList()
                                }
                            }
                        )
                    }
                }
            } else {
                // Estado Vacío (Tu diseño original)
                Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.size(120.dp).background(Pink100, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(60.dp), tint = Pink500)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Aún no tienes favoritos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Presiona el corazón ❤️ en un negocio para guardarlo aquí.", textAlign = TextAlign.Center, color = Gray600, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onNavigate(Screen.Map) }, colors = ButtonDefaults.buttonColors(containerColor = Pink500), shape = RoundedCornerShape(24.dp), modifier = Modifier.height(50.dp)) {
                        Icon(Icons.Default.Map, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Explorar Negocios", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
            AppBottomNavigationBar(currentScreen = Screen.Favorites, onNavigate = onNavigate)
        }
    }
}

@Composable
fun ModernBusinessCard(
    business: UsuarioApp, // CAMBIO: Usamos UsuarioApp
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Placeholder de imagen
            Box(modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp)).background(Amber100), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Store, null, tint = Amber600, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = business.nombreNegocio ?: "Sin nombre", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Gray900, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Blue100) {
                        Text(text = business.categoria ?: "Varios", fontSize = 12.sp, color = Blue700, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }

            IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(40.dp).background(Pink100, CircleShape)) {
                Icon(Icons.Default.Delete, null, tint = Pink500, modifier = Modifier.size(20.dp))
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar?", fontWeight = FontWeight.Bold) },
            text = { Text("¿Quieres quitar a ${business.nombreNegocio} de favoritos?") },
            confirmButton = {
                Button(onClick = { showDeleteDialog = false; onRemoveFavorite() }, colors = ButtonDefaults.buttonColors(containerColor = Pink500)) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }
}