package com.example.wire.feature.chat.presentation.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.chat.presentation.component.state.ChatUiState

/**
 * THE ELITE BANNER: Shows Gold when reconnecting, Red when offline
 */
@Composable
fun ConnectivityBanner(uiState: ChatUiState) {
    AnimatedVisibility(
        visible = uiState.isConnecting || !uiState.isConnected,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        val backgroundColor = if (uiState.isConnecting) Color(0xFFFFD700) else Color.Red.copy(alpha = 0.8f)
        val statusText = if (uiState.isConnecting) "Reconnecting to Wire..." else "Offline"

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(statusText, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}