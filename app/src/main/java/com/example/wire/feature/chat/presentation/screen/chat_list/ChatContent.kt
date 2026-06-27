package com.example.wire.feature.chat.presentation.screen.chat_list


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState
import com.example.wire.feature.chat.presentation.screen.component.ChatItem
import com.example.wire.feature.chat.presentation.screen.component.HeaderSection
import com.example.wire.feature.chat.presentation.screen.component.SearchBar
import com.example.wire.feature.wallet.presentation.components.WalletCard

@Composable
fun ChatContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onChatClick: (String) -> Unit,
    onNotificationClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HeaderSection(
                userName = uiState.displayName,
                onNotificationClick = onNotificationClick,

                )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // 1. Wallet Balance Card
            item {
                WalletCard()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Search Bar
            item {
              SearchBar()
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. Recent Section Label
            item {
                Text(
                    text = "RECENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. Chat List
            items(uiState.messages) { message ->
                ChatItem(
                    name = "Sarah K.", // Demo data for now
                    lastMessage = "Sent you $50.00",
                    time = "2m",
                    unreadCount = 2,
                    avatarColor = Color.Unspecified,
                    onClick = { onChatClick("demo_id") }
                )
            }
        }
    }
}







