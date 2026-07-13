package com.example.wire.feature.payments.presentation.component




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.R

@Composable
fun QrCodeView(
    userHandle: String,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The QR Card
        Surface(
            modifier = Modifier.size(240.dp),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // In a real app, use a QR generation library here.
                // For the prototype, we use a placeholder or your generated asset.
                Icon(
                    painter = painterResource(id = R.drawable.qr_code_2_24px),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color(0xFF1A1A2E) // Deep Navy
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Show this code to receive payment instantly",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Text(
            text = "@$userHandle • WIRE",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Outlined Share Button from design
        OutlinedButton(
            onClick = onShareClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
        ) {
            Text(
                "Share QR Code",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
