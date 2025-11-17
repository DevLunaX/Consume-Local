package mx.edu.utng.proyectotacho

import BusinessListItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.Screen // Importa tu enum Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBusinessClick: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(currentScreen = Screen.Favorites, onNavigate = onNavigate)
        }
    ) { paddingValues ->
        // Simulación: cambia esta variable a 'false' para ver el mensaje de "sin favoritos"
        val hasFavorites = true

        if (hasFavorites) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item { BusinessListItem("Panadería \"El Buen Pan\"", "★ 4.8 • Panadería • 200m", "https://i.imgur.com/GscB5hM.jpeg", onBusinessClick) }
                // Aquí podrías agregar más items de favoritos
            }
        } else {
            // Mensaje cuando no hay favoritos
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aún no tienes favoritos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Presiona el corazón ❤️ en un negocio para guardarlo aquí.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}