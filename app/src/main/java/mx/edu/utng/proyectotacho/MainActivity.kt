package mx.edu.utng.proyectotacho
import BusinessDetailScreen
import MapScreen
import UserLoginScreen
import UserRegistrationScreen
import VendorLoginScreen
import VendorRegistrationScreen
import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utng.proyectotacho.screens.auth.EditProfileUserScreen
import mx.edu.utng.proyectotacho.screens.users.FavoritesScreen
import mx.edu.utng.proyectotacho.screens.users.ProfileScreen
import mx.edu.utng.proyectotacho.screens.vendor.EditProfileScreen
import mx.edu.utng.proyectotacho.screens.vendor.ManageProductsScreen
import mx.edu.utng.proyectotacho.screens.vendor.VendorPanelScreen
import mx.edu.utng.proyectotacho.screens.vendor.ViewReviewsScreen
import mx.edu.utng.proyectotacho.screens.vendor.ViewStatisticsScreen
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

// Enum para definir las pantallas
enum class Screen {
    Welcome,
    UserLogin,              // ← NUEVO
    VendorLogin,            // ← NUEVO
    UserRegistration,
    VendorRegistration,
    Map,
    BusinessDetail,
    VendorPanel,
    Favorites,
    Profile,
    EditProfileUser,
    EditProfile,
    ManageProducts,
    ViewStatistics,
    ViewReviews
}

@Composable
fun ConsumeLocalApp() {
    var currentScreen by remember { mutableStateOf(Screen.Welcome) }

    when (currentScreen) {
        // ========== PANTALLA DE BIENVENIDA ==========
        Screen.Welcome -> WelcomeScreen(
            onNavigateToUserLogin = { currentScreen = Screen.UserLogin },
            onNavigateToVendorLogin = { currentScreen = Screen.VendorLogin }
        )

        // ========== PANTALLAS DE LOGIN ==========
        Screen.UserLogin -> UserLoginScreen(
            onBack = { currentScreen = Screen.Welcome },
            // AQUI ESTA EL CAMBIO IMPORTANTE:
            onLoginSuccess = { rol ->
                // Verificamos qué rol nos devolvió Firebase
                if (rol == "VENDEDOR") {
                    currentScreen = Screen.VendorPanel
                } else {
                    currentScreen = Screen.Map
                }
            },
            onNavigateToRegister = { currentScreen = Screen.UserRegistration }
        )

        Screen.VendorLogin -> VendorLoginScreen( // Asumiendo que actualices esta pantalla también
            onBack = { currentScreen = Screen.Welcome },
            onLoginSuccess = { rol ->
                if (rol == "VENDEDOR") {
                    currentScreen = Screen.VendorPanel
                } else {
                    // Si por error un cliente entra por el login de vendedores,
                    // el sistema es inteligente y lo manda al mapa de todos modos.
                    currentScreen = Screen.Map
                }
            },
            onNavigateToRegister = { currentScreen = Screen.VendorRegistration }
        )

        // ========== PANTALLAS DE REGISTRO ==========
        Screen.UserRegistration -> UserRegistrationScreen(
            onBack = { currentScreen = Screen.UserLogin },
            onRegisterSuccess = { currentScreen = Screen.Map }
        )

        Screen.VendorRegistration -> VendorRegistrationScreen(
            onBack = { currentScreen = Screen.VendorLogin },
            onRegisterSuccess = { currentScreen = Screen.VendorPanel }
        )

        // ========== PANTALLAS DE USUARIO ==========
        Screen.Map -> MapScreen(
            onBusinessClick = { currentScreen = Screen.BusinessDetail },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )

        Screen.BusinessDetail -> BusinessDetailScreen(
            onBack = { currentScreen = Screen.Map }
        )

        Screen.Favorites -> FavoritesScreen(
            onBusinessClick = { currentScreen = Screen.BusinessDetail },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )

        Screen.Profile -> ProfileScreen(
            onLogoutClick = { currentScreen = Screen.Welcome },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )

        // ========== PANTALLAS DE VENDEDOR ==========
        Screen.VendorPanel -> VendorPanelScreen(
            onExit = { currentScreen = Screen.Welcome },
            onNavigateToEditProfile = { currentScreen = Screen.EditProfile },
            onNavigateToManageProducts = { currentScreen = Screen.ManageProducts },
            onNavigateToStatistics = { currentScreen = Screen.ViewStatistics },
            onNavigateToReviews = { currentScreen = Screen.ViewReviews }
        )

        Screen.EditProfile -> EditProfileScreen(
            onBack = { currentScreen = Screen.VendorPanel }
        )

        Screen.ManageProducts -> ManageProductsScreen(
            onBack = { currentScreen = Screen.VendorPanel }
        )

        Screen.ViewStatistics -> ViewStatisticsScreen(
            onBack = { currentScreen = Screen.VendorPanel }
        )

        Screen.ViewReviews -> ViewReviewsScreen(
            onBack = { currentScreen = Screen.VendorPanel }
        )

        //Pantalla de edicion de datos
        Screen.EditProfileUser -> EditProfileUserScreen(
            onBack = { currentScreen = Screen.Profile }, // Si cancela, vuelve al perfil
            onUpdateSuccess = { currentScreen = Screen.Profile } // Si guarda, vuelve al perfil (y se recargará solo)
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