import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.proyectotacho.ui.theme.*

@Composable
fun WelcomeScreen(
    onNavigateToUserLogin: () -> Unit,
    onNavigateToVendorLogin: () -> Unit
) {
    Surface(
        color = Amber50,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Consume Local",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Amber900
            )
            Text(
                text = "Apoya a los negocios de tu comunidad.",
                fontSize = 16.sp,
                color = Amber800,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón para Buscar Negocios (Usuario)
            Button(
                onClick = onNavigateToUserLogin,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Buscar Negocios", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para Vendedor
            Button(
                onClick = onNavigateToVendorLogin,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gray600),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Soy un Vendedor", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onNavigateToUserLogin = {},
        onNavigateToVendorLogin = {}
    )
}