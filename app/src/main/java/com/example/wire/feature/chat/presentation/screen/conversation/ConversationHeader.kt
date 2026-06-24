package com.example.wire.feature.chat.presentation.screen.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.BackgroundDark
import com.example.wire.feature.chat.presentation.component.state.ChatUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationHeader(
    userName: String,
    onBackClick: () -> Unit,
    uiState: ChatUiState) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(32.dp).background(Color(0xFF2ECC71), CircleShape), contentAlignment = Alignment.Center) {
                    Text(userName.take(1), color = Color.White, fontSize = 14.sp)
                    val stausText = when{
                        uiState.isPeerOnline -> "Online"
                        uiState.isPeerTyping -> "Typing"
                        else-> uiState.peerLastActive
                    }
                    val statusColor = if (
                        uiState
                            .isPeerTyping ||
                        uiState.isPeerOnline) Color.Green else Color.Gray
                    Text(stausText, color = statusColor, fontSize = 10.sp)
                }

                Spacer(Modifier.width(12.dp))
                Text(userName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Videocam, null, tint = Color.White)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = { /* Implement later */ }) {
                Icon(Icons.Default.Call, null, tint = Color.White)
            }
        }
    )
}