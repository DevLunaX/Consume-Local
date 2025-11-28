import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import mx.edu.utng.proyectotacho.AuthService
import mx.edu.utng.proyectotacho.Review // Asegúrate de importar el data class
import mx.edu.utng.proyectotacho.UsuarioApp
import mx.edu.utng.proyectotacho.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BusinessDetailScreen(
    negocio: UsuarioApp?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val authService = remember { AuthService() }
    val scope = rememberCoroutineScope()

    var isFavorite by remember { mutableStateOf(false) }

    // === ESTADOS PARA RESEÑAS ===
    var reviewsList by remember { mutableStateOf<List<Review>>(emptyList()) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf<UsuarioApp?>(null) }

    // Variables calculadas
    val promedioRating = if (reviewsList.isNotEmpty()) {
        reviewsList.map { it.rating }.average()
    } else 0.0
    val totalResenas = reviewsList.size

    // CARGAR DATOS
    LaunchedEffect(negocio) {
        if (negocio != null) {
            isFavorite = authService.esFavorito(negocio.id)
            // Escuchar reseñas en tiempo real
            authService.escucharResenas(negocio.id) { reviews ->
                reviewsList = reviews
            }
            // Obtener usuario actual para saber quién escribe la reseña
            authService.obtenerUsuarioActual().onSuccess { currentUser = it }

            // === AGREGAR ESTO: Registrar visita automática ===
            authService.incrementarVisitas(negocio.id)
            // =================================================
        }
    }

    if (negocio == null) return

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // HEADER (Imagen)
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?q=80&w=1000&auto=format&fit=crop"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)), startY = 100f))
                )
            }

            // CONTENIDO
            Column(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(Gray400).align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(20.dp))

                Surface(color = Amber100, shape = RoundedCornerShape(8.dp)) {
                    Text(text = (negocio.categoria ?: "Comercio").uppercase(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Amber900, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = negocio.nombreNegocio ?: "Sin nombre", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Gray900, lineHeight = 32.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // ESTADÍSTICAS ACTUALIZADAS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Muestra el promedio real formateado a 1 decimal
                    StatItem(icon = Icons.Filled.Star, value = String.format("%.1f", promedioRating), label = "Rating", color = Amber500)
                    StatItem(icon = Icons.Outlined.Info, value = "$totalResenas", label = "Reseñas", color = Blue500)
                    StatItem(icon = Icons.Filled.LocationOn, value = "Local", label = "Guanajuato", color = Green600)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Gray100)
                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Sobre el lugar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray800)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = negocio.descripcion ?: "Sin descripción.", fontSize = 16.sp, color = Gray600, lineHeight = 24.sp)

                Spacer(modifier = Modifier.height(24.dp))
                InfoRow(icon = Icons.Filled.LocationOn, text = negocio.direccion ?: "Sin dirección")
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(icon = Icons.Filled.Call, text = negocio.telefono.ifBlank { "Sin teléfono" })

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Gray100)
                Spacer(modifier = Modifier.height(24.dp))

                // === SECCIÓN DE RESEÑAS ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Reseñas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Gray800)

                    // Botón para escribir reseña
                    TextButton(onClick = { showReviewDialog = true }) {
                        Icon(Icons.Outlined.RateReview, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Escribir opinión")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (reviewsList.isEmpty()) {
                    Text("Sé el primero en opinar sobre este lugar.", color = Gray500, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                } else {
                    // Lista de reseñas
                    reviewsList.forEach { review ->
                        ReviewItem(review)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Botones flotantes (Regresar)
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(16.dp).statusBarsPadding().background(Color.White.copy(alpha = 0.8f), CircleShape).size(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = Color.Black)
        }

        // Barra inferior (Llamar, Mapa, Favorito)
        Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(), shadowElevation = 16.dp, color = Color.White) {
            Row(modifier = Modifier.padding(16.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = { /* Llamar */ },
                    modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp), border = null, colors = ButtonDefaults.outlinedButtonColors(containerColor = Gray100)
                ) { Icon(Icons.Filled.Call, null, tint = Gray800) }

                // Botón Cómo llegar (Abre Maps y REGISTRA VISITA)
                Button(
                    onClick = {
                        // 1. REGISTRAR VISITA EN FIREBASE
                        scope.launch {
                            authService.registrarVisita(negocio.id)
                        }

                        // 2. ABRIR MAPAS (Tu lógica existente)
                        val gmmIntentUri = Uri.parse("google.navigation:q=${negocio.latitud},${negocio.longitud}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            // Fallback
                        }
                    },
                    modifier = Modifier.weight(2f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Icon(Icons.Filled.Directions, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cómo llegar", fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = {
                        scope.launch {
                            if (isFavorite) { authService.eliminarFavorito(negocio.id); isFavorite = false }
                            else { authService.agregarFavorito(negocio); isFavorite = true }
                        }
                    },
                    modifier = Modifier.size(50.dp).background(Gray100, RoundedCornerShape(12.dp))
                ) {
                    Icon(if(isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, null, tint = if(isFavorite) Color.Red else Gray600)
                }
            }
        }
    }

    // === DIÁLOGO PARA ESCRIBIR RESEÑA ===
    if (showReviewDialog) {
        ReviewDialog(
            onDismiss = { showReviewDialog = false },
            onSubmit = { rating, comment ->
                scope.launch {
                    val nuevaResena = Review(
                        businessId = negocio.id,
                        userId = currentUser?.id ?: "anon",
                        userName = currentUser?.nombre ?: currentUser?.username ?: "Usuario",
                        rating = rating,
                        comment = comment
                    )
                    authService.subirResena(nuevaResena)
                    showReviewDialog = false
                }
            }
        )
    }
}

// === COMPONENTES AUXILIARES ===

@Composable
fun ReviewItem(review: Review) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val fecha = dateFormat.format(Date(review.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = Gray50),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar (Inicial)
                Box(modifier = Modifier.size(32.dp).background(Amber100, CircleShape), contentAlignment = Alignment.Center) {
                    Text(text = review.userName.take(1).uppercase(), fontWeight = FontWeight.Bold, color = Amber900)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = review.userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = fecha, fontSize = 10.sp, color = Gray500)
                }
                Spacer(modifier = Modifier.weight(1f))
                // Estrellitas pequeñas
                Row {
                    repeat(review.rating) {
                        Icon(Icons.Filled.Star, null, tint = Amber500, modifier = Modifier.size(14.dp))
                    }
                }
            }
            if (review.comment.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = review.comment, fontSize = 14.sp, color = Gray700)
            }
        }
    }
}

@Composable
fun ReviewDialog(onDismiss: () -> Unit, onSubmit: (Int, String) -> Unit) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Calificar Experiencia", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // Estrellas interactivas
                RatingBar(rating = rating, onRatingChanged = { rating = it })

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Escribe tu opinión (opcional)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar", color = Gray600) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSubmit(rating, comment) },
                        colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                    ) {
                        Text("Publicar")
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Amber500,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Gray900)
        }
        Text(label, fontSize = 12.sp, color = Gray500)
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(Gray50, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Gray600, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = Gray700)
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    isEditable: Boolean = true
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Amber500,
                modifier = Modifier
                    .size(if (isEditable) 40.dp else 20.dp)
                    .clickable(enabled = isEditable) { onRatingChanged(i) }
            )
        }
    }
}
