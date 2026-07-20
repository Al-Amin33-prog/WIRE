package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wire.R


@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onSendMoneyClick: () -> Unit,
    onRequestMoneyClick: () -> Unit
) {
    var isWalletMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- CONTEXTUAL WALLET ACTIONS ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { isWalletMenuExpanded = !isWalletMenuExpanded }) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet",
                    tint = if (isWalletMenuExpanded)
                        Color(0xFF9370DB)
                    else Color(0xFFFFD700)
                )
            }

            if (isWalletMenuExpanded) {
                // SEND BUTTON
                TextButton(onClick = {
                    isWalletMenuExpanded = false
                    onSendMoneyClick()
                }) {
                    Text(stringResource(R.string.send),
                        color = Color(0xFF9370DB),
                        fontWeight = FontWeight.Bold)
                }
                // REQUEST BUTTON
                TextButton(onClick = {
                    isWalletMenuExpanded = false
                    onRequestMoneyClick()
                }) {
                    Text(stringResource(R.string.request),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold)
                }
            } else {
                IconButton(onClick = { /* Media logic */ }) {
                    Icon(
                        Icons.Default.Add,
                        null, tint = Color.Gray)
                }
            }
        }

        // --- TEXT FIELD ---
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(stringResource(R.string.message___), color = Color.Gray) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        // --- SEND ICON ---
        IconButton(
            onClick = onSend,
            modifier = Modifier.background(MaterialTheme
                .colorScheme.primary, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send,
                null, tint = Color.White,
                modifier = Modifier.size(20.dp))
        }
    }
}