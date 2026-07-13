package com.example.wire.feature.payments.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wire.R

@Composable
fun AmountDisplay(
    amount: String,
    availableBalance: String
) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.enter_amout),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            letterSpacing = 1.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )
            Text(
                text = if (amount.isEmpty()) "0.00" else amount,
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1A1A2E)
            )
        }

        Text(
            text = "Available: $$availableBalance",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}