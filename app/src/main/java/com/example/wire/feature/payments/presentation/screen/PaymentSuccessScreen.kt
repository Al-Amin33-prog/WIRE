package com.example.wire.feature.payments.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wire.R

@Composable
fun PaymentSuccessScreen(
    amount: String,
    recipientName: String,
    onDoneClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Branded Success Icon
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = Color(0xFFE8F5E9)
        ) {
            Icon(
                Icons.Default.Check,
                null,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                tint = Color(0xFF2E7D32)
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(stringResource(R.string.payment_sent), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)

        Text(
            "You successfully sent $$amount to $recipientName",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1424))
        ) {
            Text(stringResource(R.string.back_to_home), fontWeight = FontWeight.Bold)
        }
    }
}