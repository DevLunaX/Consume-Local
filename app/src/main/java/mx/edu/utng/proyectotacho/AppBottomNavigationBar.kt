package mx.edu.utng.proyectotacho

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.ui.theme.Amber100
import mx.edu.utng.proyectotacho.ui.theme.Amber600

@Composable
fun AppBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {

        NavigationBarItem(
            selected = currentScreen == Screen.Map,
            onClick = { onNavigate(Screen.Map) },
            icon = { Icon(Icons.Filled.Map, contentDescription = "Mapa") },
            label = { Text("Mapa", fontWeight = if (currentScreen == Screen.Map) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Amber100,
                selectedIconColor = Amber600,
                selectedTextColor = Amber600
            )
        )

        NavigationBarItem(
            selected = currentScreen == Screen.Favorites,
            onClick = { onNavigate(Screen.Favorites) },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos", fontWeight = if (currentScreen == Screen.Favorites) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Amber100,
                selectedIconColor = Amber600,
                selectedTextColor = Amber600
            )
        )

        NavigationBarItem(
            selected = currentScreen == Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontWeight = if (currentScreen == Screen.Profile) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Amber100,
                selectedIconColor = Amber600,
                selectedTextColor = Amber600
            )
        )
    }
}