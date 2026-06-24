package com.example.wire.feature.payments.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentMessageBubble(amount: String, note: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), Arrangement.End) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1405)), // Dark Amber/Gold
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(220.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("PAYMENT SENT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("$$amount", color = Color(0xFFFFD700), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(note, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
                Divider(color = Color.Gray.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                    Text(" Delivered • Stripe", color = Color(0xFFFFD700), fontSize = 11.sp)
                }
            }
        }
    }
}