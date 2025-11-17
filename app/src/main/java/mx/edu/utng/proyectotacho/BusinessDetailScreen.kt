import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utng.proyectotacho.ui.theme.*


@Composable
fun BusinessDetailScreen(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://i.imgur.com/GscB5hM.jpeg"),
                contentDescription = "Fachada de la panadería",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Panadería \"El Buen Pan\"", fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Text("★ 4.8 (32 reseñas) • Panadería Artesanal", color = Gray500, modifier = Modifier.padding(top = 4.dp))
                Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Gray600)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Av. Independencia #123", color = Gray600)
                }
                Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Green600)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Abierto ahora", color = Green600, fontWeight = FontWeight.SemiBold)
                    Text(" • Cierra 8:00 PM", color = Gray600)
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue500)) {
                    Text("Cómo llegar", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Pink500)) {
                    Text("❤️ Favorito", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Surface(color = Amber100, shape = RoundedCornerShape(8.dp)) {
                    Row(Modifier.padding(16.dp)) {
                        Text("🔥", fontSize = 20.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Oferta del Día", color = Amber900, fontWeight = FontWeight.Bold)
                            Text("¡Concha de chocolate al 2x1 hasta las 5 PM!", color = Amber800)
                        }
                    }
                }

                Text("Nuestros Productos", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProductImage("https://i.imgur.com/m4h8gq9.jpeg", Modifier.weight(1f))
                    ProductImage("https://i.imgur.com/kQJqVzT.jpeg", Modifier.weight(1f))
                    ProductImage("https://i.imgur.com/L5zX7xQ.jpeg", Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Botón de regreso
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Regresar")
        }
    }
}

@Composable
fun ProductImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp))
    )
}