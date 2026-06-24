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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wire.core.ui.theme.SurfaceDark
import com.example.wire.core.ui.theme.Violet

@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Wallet Icon
        IconButton(onClick = { /* Wallet logic */ }) {
            Icon(Icons.Default.AccountBalanceWallet, null, tint = Color(0xFFFFD700))
        }
        // Plus Icon
        IconButton(onClick = { /* Media logic */ }) {
            Icon(Icons.Default.Add, null, tint = Color.Gray)
        }

        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Message...", color = Color.Gray) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = onSend,
            modifier = Modifier.background(Violet, CircleShape)
        ) {
            Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}