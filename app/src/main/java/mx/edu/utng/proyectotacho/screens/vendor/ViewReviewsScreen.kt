// Archivo: ViewReviewsScreen.kt
package mx.edu.utng.proyectotacho.screens.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

data class Review(
    val id: Int,
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val hasResponse: Boolean = false,
    val response: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReviewsScreen(onBack: () -> Unit) {
    val reviews = remember {
        mutableStateListOf(
            Review(1, "María González", 5, "Excelente pan, muy fresco y delicioso. El servicio es muy amable.", "15/10/2025", true, "¡Muchas gracias por tu comentario! Nos alegra que hayas disfrutado nuestro pan."),
            Review(2, "Juan Pérez", 4, "Buen producto, aunque a veces hay mucha fila para comprar.", "14/10/2025"),
            Review(3, "Ana Martínez", 5, "El mejor pan de la ciudad. Las conchas son espectaculares.", "13/10/2025"),
            Review(4, "Carlos Ramírez", 3, "El pan es bueno pero los precios han subido mucho últimamente.", "12/10/2025"),
            Review(5, "Laura Sánchez", 5, "Tradición familiar, siempre tienen lo que busco.", "11/10/2025", true, "¡Gracias por tu lealtad! Es un placer atenderte.")
        )
    }

    val averageRating = reviews.map { it.rating }.average()
    val totalReviews = reviews.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reseñas de Clientes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen de calificaciones
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                String.format("%.1f", averageRating),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        if (index < averageRating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = null,
                                        tint = Yellow500,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Text("$totalReviews reseñas", fontSize = 12.sp)
                        }

                        Divider(
                            modifier = Modifier
                                .height(80.dp)
                                .width(1.dp)
                        )

                        Column {
                            RatingBar(5, reviews.count { it.rating == 5 }, totalReviews)
                            RatingBar(4, reviews.count { it.rating == 4 }, totalReviews)
                            RatingBar(3, reviews.count { it.rating == 3 }, totalReviews)
                            RatingBar(2, reviews.count { it.rating == 2 }, totalReviews)
                            RatingBar(1, reviews.count { it.rating == 1 }, totalReviews)
                        }
                    }
                }
            }

            // Lista de reseñas
            items(reviews) { review ->
                ReviewCard(review = review)
            }
        }
    }
}

@Composable
fun RatingBar(stars: Int, count: Int, total: Int) {
    Row(
        modifier = Modifier.width(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("$stars", fontSize = 12.sp, modifier = Modifier.width(12.dp))
        Icon(Icons.Filled.Star, contentDescription = null, tint = Yellow500, modifier = Modifier.size(12.dp))
        LinearProgressIndicator(
            progress = if (total > 0) count.toFloat() / total else 0f,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
        )
        Text("$count", fontSize = 12.sp, modifier = Modifier.width(20.dp))
    }
}

@Composable
fun ReviewCard(review: Review) {
    var showResponseDialog by remember { mutableStateOf(false) }
    var responseText by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header de la reseña
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(review.userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(review.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row {
                    repeat(5) { index ->
                        Icon(
                            if (index < review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Yellow500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Comentario
            Text(review.comment, fontSize = 14.sp)

            // Respuesta existente
            if (review.hasResponse && review.response != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Respuesta del negocio:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            review.response,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            } else {
                // Botón para responder
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { showResponseDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Responder a esta reseña")
                }
            }
        }
    }

    // Diálogo para responder
    if (showResponseDialog) {
        AlertDialog(
            onDismissRequest = { showResponseDialog = false },
            title = { Text("Responder Reseña") },
            text = {
                Column {
                    Text("Respuesta a ${review.userName}:", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = responseText,
                        onValueChange = { responseText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        placeholder = { Text("Escribe tu respuesta...") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Aquí iría la lógica para guardar la respuesta
                        showResponseDialog = false
                        responseText = ""
                    },
                    enabled = responseText.isNotBlank()
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResponseDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}