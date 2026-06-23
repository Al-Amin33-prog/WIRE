package com.example.wire.feature.chat.presentation.screen.chat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.*
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState

@Composable
fun ChatContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onChatClick: (String) -> Unit
) {
    Scaffold(
        containerColor = BackgroundDark, // Using your theme color
        topBar = {
            HeaderSection( userName = uiState.displayName)
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
                    avatarColor = Color(0xFF2ECC71),
                    onClick = { onChatClick("demo_id") }
                )
            }
        }
    }
}

@Composable
fun HeaderSection(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Good morning, $userName 👋", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "MESSAGES",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
        Row {
            IconButton(onClick = {}, modifier = Modifier.background(SurfaceDark, CircleShape)) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box {
                IconButton(onClick = {}, modifier = Modifier.background(SurfaceDark, CircleShape)) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                }
                // Notification Badge
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text("3", color = Color.White, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun WalletCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E153D)) // Deep Purple
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("WALLET BALANCE", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$4,280", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(".50", color = Color.White.copy(alpha = 0.6f), fontSize = 24.sp, modifier = Modifier.padding(bottom = 4.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowUpward, null, modifier = Modifier.size(16.dp))
                        Text(" Send")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowDownward, null, modifier = Modifier.size(16.dp))
                        Text(" Receive")
                    }
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Wallet →", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Search conversations...", color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ChatItem(
    name: String,
    lastMessage: String,
    time: String,
    unreadCount: Int,
    avatarColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            // Online status dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color.Green, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = lastMessage, color = Color.Gray, fontSize = 14.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = time, color = Color.Gray, fontSize = 12.sp)
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                        .background(Violet, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(unreadCount.toString(), color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}