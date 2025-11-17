package mx.edu.utng.proyectotacho
import BusinessDetailScreen
import MapScreen
import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utng.proyectotacho.ui.theme.ProyectoTachoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoTachoTheme {
                ConsumeLocalApp()
            }
        }
    }
}

// Enum para definir las pantallas de forma segura
// 1. Actualiza tu enum
enum class Screen {
    Welcome,
    Map,
    BusinessDetail,
    VendorPanel,
    Favorites, // <-- Añade esta línea
    Profile,  // <-- Añade esta línea

    EditProfile,

    ManageProducts,

    ViewStatistics,

    ViewReviews
}

@Composable
fun ConsumeLocalApp() {
    var currentScreen by remember { mutableStateOf(Screen.Welcome) }

    when (currentScreen) {
        Screen.Welcome -> WelcomeScreen(
            onNavigateToMap = { currentScreen = Screen.Map },
            onNavigateToVendorPanel = { currentScreen = Screen.VendorPanel }
        )
        Screen.Map -> MapScreen(
            onBusinessClick = { currentScreen = Screen.BusinessDetail },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )
        Screen.BusinessDetail -> BusinessDetailScreen(
            onBack = { currentScreen = Screen.Map }
        )
        // --- SECCIÓN MODIFICADA ---
        Screen.VendorPanel -> VendorPanelScreen(
            onExit = { currentScreen = Screen.Welcome },
            // Aquí conectamos cada acción con un cambio de pantalla
            onNavigateToEditProfile = { currentScreen = Screen.EditProfile },
            onNavigateToManageProducts = { currentScreen = Screen.ManageProducts },
            onNavigateToStatistics = { currentScreen = Screen.ViewStatistics },
            onNavigateToReviews = { currentScreen = Screen.ViewReviews }
        )
        // --- CASOS NUEVOS ---
        Screen.EditProfile -> EditProfileScreen(onBack = { currentScreen = Screen.VendorPanel })
        Screen.ManageProducts -> ManageProductsScreen(onBack = { currentScreen = Screen.VendorPanel })
        Screen.ViewStatistics -> ViewStatisticsScreen(onBack = { currentScreen = Screen.VendorPanel })
        Screen.ViewReviews -> ViewReviewsScreen(onBack = { currentScreen = Screen.VendorPanel })

        // Tus otras pantallas
        Screen.Favorites -> FavoritesScreen(
            onBusinessClick = { currentScreen = Screen.BusinessDetail },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )
        Screen.Profile -> ProfileScreen(
            onLogoutClick = { currentScreen = Screen.Welcome },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProyectoTachoTheme {
        ConsumeLocalApp()
    }
}