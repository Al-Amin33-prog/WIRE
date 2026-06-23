package com.example.wire.feature.chat.presentation.screen.conversation



import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.ui.theme.*
import com.example.wire.feature.chat.presentation.component.event.ChatUiEvent
import com.example.wire.feature.chat.presentation.component.state.ChatUiState

@Composable
fun ConversationContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            ConversationHeader(
                userName = "Sarah K.", // In prod, get from uiState
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            ChatInputBar(
                text = uiState.messageText,
                onTextChange = { onEvent(ChatUiEvent.MessageChanged(it)) },
                onSend = { onEvent(ChatUiEvent.SendMessage) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            reverseLayout = false // Set to true if you want items to start from bottom
        ) {
            item {
                Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text("Today", color = Color.Gray, fontSize = 12.sp)
                }
            }

            items(uiState.messages) { message ->
                val isMe = message.senderId == "me" // Replace with actual logic
                MessageBubble(message = message.content, isMe = isMe)
            }

            // DEMO: Payment Message Bubble (Matching your screenshot)
            item {
                PaymentMessageBubble(amount = "75.00", note = "For dinner last night")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationHeader(userName: String, onBackClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(32.dp).background(Color(0xFF2ECC71), CircleShape), contentAlignment = Alignment.Center) {
                    Text(userName.take(1), color = Color.White, fontSize = 14.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text(userName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

@Composable
fun MessageBubble(message: String, isMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            Box(Modifier.size(28.dp).background(Color(0xFF2ECC71), CircleShape), contentAlignment = Alignment.Center) {
                Text("S", color = Color.White, fontSize = 12.sp)
            }
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            color = if (isMe) Violet else SurfaceDark,
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            )
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun PaymentMessageBubble(amount: String, note: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), Arrangement.End) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1405)), // Dark Amber/Gold
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(220.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("PAYMENT SENT", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("$$amount", color = Color(0xFFFFD700), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(note, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
                Divider(color = Color.Gray.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
                    Text(" Delivered • Stripe", color = Color(0xFFFFD700), fontSize = 11.sp)
                }
            }
        }
    }
}

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