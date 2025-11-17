package mx.edu.utng.proyectotacho.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.Screen
import mx.edu.utng.proyectotacho.ui.theme.*

@Composable
fun AppBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 50.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            tonalElevation = 0.dp
        ) {
            // Mapa
            NavigationBarItem(
                selected = currentScreen == Screen.Map,
                onClick = { onNavigate(Screen.Map) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == Screen.Map) Icons.Filled.Map else Icons.Outlined.Map,
                        contentDescription = "Mapa",
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = {
                    Text(
                        text = "Explorar",
                        fontWeight = if (currentScreen == Screen.Map) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Amber600,
                    selectedTextColor = Amber600,
                    indicatorColor = Amber100,
                    unselectedIconColor = Gray500,
                    unselectedTextColor = Gray500
                )
            )

            // Favoritos
            NavigationBarItem(
                selected = currentScreen == Screen.Favorites,
                onClick = { onNavigate(Screen.Favorites) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == Screen.Favorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favoritos",
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = {
                    Text(
                        text = "Favoritos",
                        fontWeight = if (currentScreen == Screen.Favorites) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Pink500,
                    selectedTextColor = Pink500,
                    indicatorColor = Pink100,
                    unselectedIconColor = Gray500,
                    unselectedTextColor = Gray500
                )
            )

            // Perfil
            NavigationBarItem(
                selected = currentScreen == Screen.Profile,
                onClick = { onNavigate(Screen.Profile) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == Screen.Profile) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = {
                    Text(
                        text = "Perfil",
                        fontWeight = if (currentScreen == Screen.Profile) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue600,
                    selectedTextColor = Blue600,
                    indicatorColor = Blue100,
                    unselectedIconColor = Gray500,
                    unselectedTextColor = Gray500
                )
            )
        }
    }
}