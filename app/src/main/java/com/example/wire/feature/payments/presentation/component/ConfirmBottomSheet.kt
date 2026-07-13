package com.example.wire.feature.payments.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wire.R
import com.example.wire.feature.payments.presentation.state.PaymentUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBottomSheet(
    uiState: PaymentUiState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier
            .systemBarsPadding()
            .padding(24.dp)
            .fillMaxWidth()) {
            Text(stringResource(R.string.confirm_payment), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            PaymentDetailRow(
                stringResource(R.string.to),
                uiState.recipientName)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp)
            PaymentDetailRow(
                stringResource(R.string.amount),
                "$${uiState.amount}", isBold = true)
            HorizontalDivider(modifier = Modifier
                .padding(vertical = 12.dp),
                thickness = 0.5.dp)
            PaymentDetailRow(
                stringResource(R.string.fee),
                stringResource(R.string.free),
                valueColor = Color(0xFF2E7D32))

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1424)),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isLoading) CircularProgressIndicator(color = Color.White)
                else Text(stringResource(R.string.confirm_send), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PaymentDetailRow(
    label: String,
    value: String, isBold:
    Boolean = false,
    valueColor: Color = Color.Black
) {
    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = if (isBold) FontWeight.Black
        else FontWeight.Bold, color = valueColor)
    }
}