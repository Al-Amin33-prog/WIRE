package com.example.wire.feature.wallet.presentation.components




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.BrandGold
import com.example.wire.core.ui.theme.BrandNavy

@Composable
fun BalanceCard(
    amount: String,
    onSend: () -> Unit,
    onReceive: () -> Unit,
    onTopUp: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = BrandNavy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TOTAL BALANCE",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 1.sp
            )

            // Dynamic Balance Text with Gold Symbol
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$", color = BrandGold, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = amount, color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black)
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WalletActionButton(Modifier.weight(1f), "Send", Icons.Default.ArrowUpward, onSend)
                WalletActionButton(Modifier.weight(1f), "Receive", Icons.Default.ArrowDownward, onReceive)
                WalletActionButton(Modifier.weight(1f), "Top Up", Icons.Default.Add, onTopUp)
            }
        }
    }
}

@Composable
fun WalletActionButton(
    modifier: Modifier,
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = BrandGold, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}