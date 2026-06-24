package com.example.wire.feature.chat.presentation.screen.conversation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Ensure these are your custom domain imports
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.core.ui.theme.SurfaceDark
import com.example.wire.core.ui.theme.Violet
import com.example.wire.feature.chat.domain.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    onLongClick: () -> Unit
) {
    val bubbleColor = if (message.isDeleted) Color.DarkGray
    else (if (isMe) Violet else SurfaceDark)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End
        else Alignment.Start
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            modifier = Modifier.combinedClickable(
                onClick = { /* Nothing */ },
                onLongClick = onLongClick
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (message.isDeleted) {
                    Text(
                        text = "This message was deleted",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    // --- HANDLE MESSAGE TYPE ---
                    when (message.type) {
                        MessageType.TEXT -> {
                            Text(text = message.content, color = Color.White)
                        }
                        // You can add Image/Video/Payment cases here later
                        else -> {
                            Text("Supported in next update", color = Color.Gray, fontSize = 12.sp)
                        }
                    }

                    // --- FOOTER (Status + Time) ---
                    Row(
                        modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.isEdited) {
                            Text(
                                text = "Edited",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        }

                        Text(
                            text = formatTimestamp(message.timestamp),
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        if (isMe && !message.isDeleted) {
                            Spacer(modifier = Modifier.width(4.dp))
                            // Handle MessageStatus (Currently logic for SENT)
                            Icon(
                                imageVector = if (message.status == MessageStatus.SENT)
                                    Icons.Default.Check else Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}