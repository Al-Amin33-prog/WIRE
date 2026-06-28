package com.example.wire.feature.wallet.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.Amber
import com.example.wire.core.ui.theme.SurfaceDark

@Composable
fun WalletCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E153D)) // Deep Purple
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("WALLET BALANCE", color = Color.Gray,
                fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$4,280",
                    color = Color.White, fontSize = 32.sp,
                    fontWeight = FontWeight.Bold)
                Text(".50",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 4.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowUpward, null, modifier = Modifier.size(16.dp))
                        Text(" Send")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowDownward, null, modifier = Modifier.size(16.dp))
                        Text(" Receive")
                    }
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Wallet →", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}