package com.example.wire.feature.profile.presentation.component



import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.profile.domain.model.Profile

@Composable
fun ProfileStatsCard(
    profile: Profile,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .border(
                width = 1.5.dp,
                color = Color(0xFF0B1A3E).copy(alpha = 0.09f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem(
                value = profile.contactCount.toString(),
                label = "Contacts",
                modifier = Modifier.weight(1f)
            )
            VerticalDivider()
            StatItem(
                value = "$${formatAmount(profile.totalSent)}",
                label = "Sent",
                modifier = Modifier.weight(1f)
            )
            VerticalDivider()
            StatItem(
                value = "$${formatAmount(profile.totalReceived)}",
                label = "Received",
                modifier = Modifier.weight(1f)
            )
            VerticalDivider()
            StatItem(
                value = profile.totalTransfers.toString(),
                label = "Transfers",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B1A3E)
        )
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9AA0B4),
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(48.dp)
            .padding(vertical = 8.dp),
    ) {
        Divider(
            modifier = Modifier.fillMaxHeight(),
            color = Color(0xFF0B1A3E).copy(alpha = 0.09f),
            thickness = 1.dp
        )
    }
}

private fun formatAmount(amount: Double): String {
    return when {
        amount >= 1000 -> "${(amount / 1000).let {
            if (it % 1 == 0.0) it.toInt().toString() else String.format("%.1f", it)
        }}K"
        else -> amount.toInt().toString()
    }
}