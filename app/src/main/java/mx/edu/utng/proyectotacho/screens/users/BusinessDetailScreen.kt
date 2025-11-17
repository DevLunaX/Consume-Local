import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utng.proyectotacho.ui.theme.*

@Composable
fun BusinessDetailScreen(onBack: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del negocio
            Image(
                painter = rememberAsyncImagePainter("https://i.imgur.com/GscB5hM.jpeg"),
                contentDescription = "Fachada de la panadería",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // Información del negocio
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Panadería \"El Buen Pan\"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Gray700
                )

                Text(
                    text = "★ 4.8 (32 reseñas) • Panadería Artesanal",
                    color = Gray500,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Ubicación
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = Gray600,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Av. Independencia #123", color = Gray600, fontSize = 15.sp)
                }

                // Horario
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = Green600,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Abierto ahora",
                        color = Green600,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(" • Cierra 8:00 PM", color = Gray600, fontSize = 15.sp)
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO: Abrir Google Maps */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cómo llegar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Button(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFavorite) Pink500 else Pink500
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isFavorite) "❤️ Guardado" else "🤍 Favorito",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Oferta del día
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Amber100),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔥", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Oferta del Día",
                                color = Amber900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "¡Concha de chocolate al 2x1 hasta las 5 PM!",
                                color = Amber800,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Productos
                Text(
                    text = "Nuestros Productos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Gray700,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductImage("https://i.imgur.com/m4h8gq9.jpeg", Modifier.weight(1f))
                    ProductImage("https://i.imgur.com/kQJqVzT.jpeg", Modifier.weight(1f))
                    ProductImage("https://i.imgur.com/L5zX7xQ.jpeg", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Botón de regreso (flotante)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = Gray700
            )
        }
    }
}

@Composable
fun ProductImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
    )
}