package com.example.wire.feature.chat.presentation.screen.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("RememberReturnType")
@Composable
fun ChatItem(
    name: String,
    lastMessage: String?,
    time: String,
    unreadCount: Int,
    avatarColor: Color,
    onClick: () -> Unit
) {
    val timeText = time

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box { // 1. Wrapper Box
            Box( // 2. Avatar Box
                modifier = Modifier
                    .size(56.dp)
                    .background(avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.toString()?.uppercase() ?: "?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Online status dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color.Green, CircleShape)
                    .align(Alignment.BottomEnd) // now works
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1
            )
            Text(
                text = lastMessage ?: "No messages yet",
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = timeText, color = Color.Gray, fontSize = 12.sp)
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}