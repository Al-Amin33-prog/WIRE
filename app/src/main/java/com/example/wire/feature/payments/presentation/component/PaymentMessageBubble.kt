package com.example.wire.feature.payments.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.R

@Composable
fun PaymentMessageBubble(
    amount: String,note: String?,
    isMe: Boolean
) {
    // NovaPay Style: Dark Navy for Sender, Soft Gray for Receiver
    val backgroundColor = if (isMe) Color(0xFF1A1A2E) else Color(0xFFF2F2F7)
    val contentColor = if (isMe) Color.White else Color.Black

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isMe) 20.dp else 4.dp,
            bottomEnd = if (isMe) 4.dp else 20.dp
        ),
        modifier = Modifier.padding(vertical = 4.dp).widthIn(max = 300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFFFFD700), // Branded Gold
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text =
                        if (isMe) stringResource(R.string.sent_payment)
                        else stringResource(R.string.received_payment),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor.copy(alpha = 0.6f)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "$$amount",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = contentColor
            )

            if (!note.isNullOrBlank()) {
                Text(
                    text = note,
                    fontSize = 14.sp,
                    color = contentColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RequestMessageBubble(
    amount: String,
    isMe: Boolean,
    onPayClick: () -> Unit
) {
    Surface(
        border = BorderStroke(1.dp, Color(0xFF9370DB)), // Branded Purple Outline
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(vertical = 4.dp).widthIn(max = 300.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.payment_request),
                color = Color(0xFF9370DB),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black
            )

            Text("$$amount", fontSize = 28.sp, fontWeight = FontWeight.Black)

            if (!isMe) {
                // If I received the request, show the Pay button
                Button(
                    onClick = onPayClick,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9370DB)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.pay_now), fontWeight = FontWeight.Bold)
                }
            } else {
                // If I sent the request, show waiting status
                Text(
                    stringResource(R.string.waiting_for_payment),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}