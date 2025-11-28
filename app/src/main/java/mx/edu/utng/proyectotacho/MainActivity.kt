package mx.edu.utng.proyectotacho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import mx.edu.utng.proyectotacho.ui.theme.ProyectoTachoTheme

// === IMPORTS CORRECTOS DE TUS PANTALLAS ===
// Auth
import mx.edu.utng.proyectotacho.screens.auth.EditProfileUserScreen
// Users
import mx.edu.utng.proyectotacho.screens.users.FavoritesScreen
import mx.edu.utng.proyectotacho.screens.users.ProfileScreen
// Vendor
import mx.edu.utng.proyectotacho.screens.vendor.EditVendorProfileScreen
import mx.edu.utng.proyectotacho.screens.vendor.ManageProductsScreen
import mx.edu.utng.proyectotacho.screens.vendor.VendorPanelScreen
import mx.edu.utng.proyectotacho.screens.vendor.VendorRegistrationScreen // <--- AQUÍ ESTÁ EL BUENO
import mx.edu.utng.proyectotacho.screens.vendor.ViewReviewsScreen
import mx.edu.utng.proyectotacho.screens.vendor.ViewStatisticsScreen

// Imports de pantallas que quizás no moviste de carpeta (Si te marcan rojo, avísame)
import BusinessDetailScreen
import MapScreen
import UserLoginScreen
import UserRegistrationScreen
import VendorLoginScreen
import WelcomeScreen

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
    UserLogin,
    VendorLogin,
    UserRegistration,
    VendorRegistration,
    Map,
    BusinessDetail,
    VendorPanel,
    Favorites,
    Profile,
    EditProfileUser, // Para Usuario
    EditProfile,     // Para Vendedor
    ManageProducts,
    ViewStatistics,
    ViewReviews
}

@Composable
fun ConsumeLocalApp() {
    var currentScreen by remember { mutableStateOf(Screen.Welcome) }

    // === VARIABLE NUEVA PARA GUARDAR EL NEGOCIO SELECCIONADO ===
    var selectedBusiness by remember { mutableStateOf<UsuarioApp?>(null) }
    // ==========================================================

    when (currentScreen) {
        // ========== PANTALLA DE BIENVENIDA ==========
        Screen.Welcome -> WelcomeScreen(
            onNavigateToUserLogin = { currentScreen = Screen.UserLogin },
            onNavigateToVendorLogin = { currentScreen = Screen.VendorLogin }
        )

        // ========== PANTALLAS DE LOGIN ==========
        Screen.UserLogin -> UserLoginScreen(
            onBack = { currentScreen = Screen.Welcome },
            onLoginSuccess = { rol ->
                if (rol == "VENDEDOR") {
                    currentScreen = Screen.VendorPanel
                } else {
                    currentScreen = Screen.Map
                }
            },
            onNavigateToRegister = { currentScreen = Screen.UserRegistration }
        )

        Screen.VendorLogin -> VendorLoginScreen(
            onBack = { currentScreen = Screen.Welcome },
            onLoginSuccess = { rol ->
                if (rol == "VENDEDOR") {
                    currentScreen = Screen.VendorPanel
                } else {
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

        // ACTUALIZA EL MAPA
        Screen.Map -> MapScreen(
            onBusinessClick = { negocio ->
                selectedBusiness = negocio // 1. Guardamos el negocio
                currentScreen = Screen.BusinessDetail // 2. Cambiamos de pantalla
            },
            onNavigate = { newScreen -> currentScreen = newScreen }
        )

        // ACTUALIZA EL DETALLE
        Screen.BusinessDetail -> BusinessDetailScreen(
            negocio = selectedBusiness, // 3. Le pasamos el negocio guardado
            onBack = { currentScreen = Screen.Map }
        )

        // En MainActivity.kt
        Screen.Favorites -> FavoritesScreen(
            onBusinessClick = { negocio ->
                selectedBusiness = negocio // Reusamos la variable que creamos antes
                currentScreen = Screen.BusinessDetail
            },
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
            onNavigateToStatistics = { currentScreen = Screen.ViewStatistics },
            onNavigateToReviews = { currentScreen = Screen.ViewReviews }
        )

        // AQUÍ USAMOS LA PANTALLA DEL VENDEDOR RENOMBRADA
        Screen.EditProfile -> EditVendorProfileScreen(
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

        // ========== PANTALLA DE EDICIÓN USUARIO ==========
        Screen.EditProfileUser -> EditProfileUserScreen(
            onBack = { currentScreen = Screen.Profile },
            onUpdateSuccess = { currentScreen = Screen.Profile }
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