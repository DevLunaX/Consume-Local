package mx.edu.utng.proyectotacho

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.Screen // Importa tu enum Screen
import mx.edu.utng.proyectotacho.ui.theme.Gray100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(currentScreen = Screen.Profile, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Círculo de la foto de perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Gray100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Usuario Tacho", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("usuario.tacho@email.com", fontSize = 16.sp, color = MaterialTheme.colorScheme.outline)

            Spacer(modifier = Modifier.height(48.dp))

            // Opciones del menú
            ProfileOptionItem(icon = Icons.Outlined.Person, text = "Editar Información")
            Divider(color = Gray100)
            ProfileOptionItem(icon = Icons.Outlined.Settings, text = "Configuración")
            Divider(color = Gray100)
            ProfileOptionItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                text = "Cerrar Sesión",
                textColor = MaterialTheme.colorScheme.error,
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = textColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = textColor, modifier = Modifier.weight(1f))
        if (onClick != {}) { // Solo muestra la flecha si es una opción clickeable
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}