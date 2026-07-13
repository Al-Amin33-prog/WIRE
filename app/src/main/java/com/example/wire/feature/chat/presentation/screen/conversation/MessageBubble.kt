package com.example.wire.feature.chat.presentation.screen.conversation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wire.core.common.util.formatTimestamp
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import com.example.wire.feature.payments.presentation.component.PaymentMessageBubble
import com.example.wire.feature.payments.presentation.component.RequestMessageBubble

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    onLongClick: () -> Unit,
    onPayRequestClick: (amount: String) -> Unit // Added for Request functionality
) {
    val colorScheme = MaterialTheme.colorScheme

    // THEME-AWARE COLORS: No more hardcoding
    // In MessageBubble.kt, replace hardcoded colors:
    val bubbleColor = when {
        message.isDeleted -> MaterialTheme.colorScheme.outlineVariant
        isMe -> MaterialTheme.colorScheme.primary       // Your Gold/Navy
        else -> MaterialTheme.colorScheme.surfaceVariant // Your Soft Gray/Cream
    }

    val contentColor = if (isMe) colorScheme.onPrimary else colorScheme.onSecondaryContainer

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        // Handle specialized Payment/Request types
        when (message.type) {
            MessageType.PAYMENT -> {
                PaymentMessageBubble(
                    amount = message.content,
                    note = message.metadata?.get("note"),
                    isMe = isMe
                )
            }
            MessageType.REQUEST -> {
                RequestMessageBubble(
                    amount = message.content,
                    isMe = isMe,
                    onPayClick = { onPayRequestClick(message.content) }
                )
            }
            else -> {
                // STANDARD TEXT BUBBLE
                Surface(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    ),
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { },
                            onLongClick = onLongClick
                        )
                        .widthIn(max = 280.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        if (message.isDeleted) {
                            Text(
                                text ="This message was deleted",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.outline,
                                fontStyle = FontStyle.Italic
                            )
                        } else {
                            Text(
                                text = message.content,
                                color = contentColor,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            // Footer: Time and Status
                            Row(
                                modifier = Modifier.align(Alignment.End).padding(top = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatTimestamp(message.timestamp),
                                    fontSize = 10.sp,
                                    color = contentColor.copy(alpha = 0.6f)
                                )
                                if (isMe) {
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        imageVector = if (message.status == MessageStatus.SENT)
                                            Icons.Default.Check else Icons.Default.AccessTime,
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = contentColor.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}