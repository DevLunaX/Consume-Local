package mx.edu.utng.proyectotacho.screens.users

import androidx.compose.animation.*
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
import mx.edu.utng.proyectotacho.components.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.ui.theme.*

data class FavoriteBusiness(
    val id: Int,
    val name: String,
    val rating: String,
    val category: String,
    val distance: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBusinessClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    // Lista de favoritos (simulada)
    val favoritesList = remember {
        mutableStateListOf(
            FavoriteBusiness(
                1,
                "Panadería \"El Buen Pan\"",
                "4.8",
                "Panadería",
                "200m",
                "https://i.imgur.com/GscB5hM.jpeg"
            ),
            FavoriteBusiness(
                2,
                "Frutería \"La Huerta\"",
                "4.9",
                "Frutas y Verduras",
                "350m",
                "https://i.imgur.com/u5u06xV.jpeg"
            ),
            FavoriteBusiness(
                3,
                "Tortillería \"Don José\"",
                "4.7",
                "Tortillería",
                "150m",
                "https://i.imgur.com/GscB5hM.jpeg"
            ),
            FavoriteBusiness(
                4,
                "Carnicería \"La Especial\"",
                "4.6",
                "Carnicería",
                "400m",
                "https://i.imgur.com/u5u06xV.jpeg"
            )
        )
    }

    val hasFavorites = favoritesList.isNotEmpty()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Pink500, Pink600)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onNavigate(Screen.Map) },
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Título y contador
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = "Mis Favoritos",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${favoritesList.size} ${if (favoritesList.size == 1) "negocio" else "negocios"}",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Contenido
            if (hasFavorites) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = favoritesList,
                        key = { it.id }
                    ) { business ->
                        ModernBusinessCard(
                            business = business,
                            onClick = onBusinessClick,
                            onRemoveFavorite = { favoritesList.remove(business) }
                        )
                    }
                }
            } else {
                // Estado vacío
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Pink100, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Pink500
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Aún no tienes favoritos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Presiona el corazón ❤️ en un negocio para guardarlo aquí.",
                        textAlign = TextAlign.Center,
                        color = Gray600,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onNavigate(Screen.Map) },
                        colors = ButtonDefaults.buttonColors(containerColor = Pink500),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Explorar Negocios", fontWeight = FontWeight.Bold)
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
                currentScreen = Screen.Favorites,
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
fun ModernBusinessCard(
    business: FavoriteBusiness,
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del negocio
            Box {
                Image(
                    painter = rememberAsyncImagePainter(business.imageUrl),
                    contentDescription = business.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                // Badge de calificación
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Amber500
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White
                        )
                        Text(
                            text = business.rating,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del negocio
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = business.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Gray900,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Blue100
                    ) {
                        Text(
                            text = business.category,
                            fontSize = 12.sp,
                            color = Blue700,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "•",
                        color = Gray400,
                        fontSize = 12.sp
                    )

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Gray600
                    )

                    Text(
                        text = business.distance,
                        fontSize = 13.sp,
                        color = Gray600
                    )
                }
            }

            // Botón de eliminar favorito
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .size(40.dp)
                    .background(Pink100, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Eliminar de favoritos",
                    tint = Pink500,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Diálogo de confirmación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Pink500
                )
            },
            title = {
                Text("¿Eliminar de favoritos?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("¿Estás seguro de que quieres eliminar \"${business.name}\" de tus favoritos?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onRemoveFavorite()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Pink500)
                ) {
                    Text("Eliminar")
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