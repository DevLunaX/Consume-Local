package mx.edu.utng.proyectotacho.screens.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.components.AppBottomNavigationBar
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Amber500, Amber600)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto de perfil con borde
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color.White, CircleShape)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Amber100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.size(60.dp),
                                tint = Amber600
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Mau",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "mau@email.com",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de editar perfil
                    Button(
                        onClick = { /* TODO: Ir a editar perfil */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Amber600
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar Perfil", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Estadísticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        icon = Icons.Filled.Favorite,
                        value = "12",
                        label = "Favoritos",
                        color = Pink500
                    )
                    StatCard(
                        icon = Icons.Filled.Star,
                        value = "8",
                        label = "Reseñas",
                        color = Amber500
                    )
                    StatCard(
                        icon = Icons.Filled.ShoppingBag,
                        value = "25",
                        label = "Visitas",
                        color = Blue500
                    )
                }

                // Sección: Mi Actividad
                Text(
                    text = "Mi Actividad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray700,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        ModernProfileOption(
                            icon = Icons.Filled.Favorite,
                            text = "Mis Favoritos",
                            description = "12 negocios guardados",
                            iconColor = Pink500,
                            onClick = { onNavigate(Screen.Favorites) }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.Star,
                            text = "Mis Reseñas",
                            description = "8 reseñas escritas",
                            iconColor = Amber500,
                            onClick = { /* TODO */ }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.LocationOn,
                            text = "Lugares Visitados",
                            description = "25 negocios",
                            iconColor = Blue500,
                            onClick = { /* TODO */ }
                        )
                    }
                }

                // Sección: Preferencias
                Text(
                    text = "Preferencias",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray700,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        ModernProfileOption(
                            icon = Icons.Filled.Notifications,
                            text = "Notificaciones",
                            description = "Gestionar alertas",
                            iconColor = Blue500,
                            onClick = { /* TODO */ }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.Language,
                            text = "Idioma",
                            description = "Español",
                            iconColor = Green600,
                            onClick = { /* TODO */ }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.DarkMode,
                            text = "Tema",
                            description = "Claro",
                            iconColor = Gray700,
                            onClick = { /* TODO */ }
                        )
                    }
                }

                // Sección: Ayuda y Soporte
                Text(
                    text = "Ayuda y Soporte",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray700,
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        ModernProfileOption(
                            icon = Icons.Filled.Help,
                            text = "Centro de Ayuda",
                            description = "FAQs y soporte",
                            iconColor = Blue500,
                            onClick = { /* TODO */ }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.Info,
                            text = "Acerca de",
                            description = "Versión 1.0.0",
                            iconColor = Gray600,
                            onClick = { /* TODO */ }
                        )
                        Divider(color = Gray100, modifier = Modifier.padding(horizontal = 16.dp))
                        ModernProfileOption(
                            icon = Icons.Filled.Share,
                            text = "Compartir App",
                            description = "Invita a tus amigos",
                            iconColor = Green600,
                            onClick = { /* TODO */ }
                        )
                    }
                }

                // Botón de cerrar sesión
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                        .padding(bottom = 100.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Cerrar Sesión",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
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
                currentScreen = Screen.Profile,
                onNavigate = onNavigate
            )
        }
    }

    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text("¿Cerrar sesión?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("¿Estás seguro de que quieres cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = Modifier.width(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Gray700
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Gray600
            )
        }
    }
}

@Composable
fun ModernProfileOption(
    icon: ImageVector,
    text: String,
    description: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = iconColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Gray700
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Gray600
            )
        }

        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = Gray400
        )
    }
}