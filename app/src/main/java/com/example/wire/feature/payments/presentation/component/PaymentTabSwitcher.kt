package com.example.wire.feature.payments.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.wire.feature.payments.presentation.state.PaymentTab


@Composable
fun PaymentTabSwitcher(
    selectedTab: PaymentTab,
    onTabSelected: (PaymentTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {
        TabItem(
            label = stringResource(R.string.keypad),
            isSelected = selectedTab == PaymentTab.KEYPAD,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(PaymentTab.KEYPAD) }
        )
        TabItem(
            label = stringResource(R.string.qr_code),
            isSelected = selectedTab == PaymentTab.QR_CODE,
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(PaymentTab.QR_CODE) }
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        onClick = onClick,
        color = if (isSelected) Color.White else Color.Transparent,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) Color.Black else Color.Gray
            )
        }
    }
}