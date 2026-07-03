
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.wire.core.ui.theme.BrandNavy
import com.example.wire.feature.chat.domain.model.Message
import com.example.wire.feature.chat.domain.model.MessageStatus
import com.example.wire.feature.chat.domain.model.MessageType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import com.example.wire.core.common.util.formatTimestamp

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    onLongClick: () -> Unit
) {

    val bubbleColor = when {
        message.isDeleted -> Color.LightGray.copy(alpha = 0.4f)
        isMe -> BrandNavy // Outgoing: Navy Blue
        else -> Color.White // Incoming: Pure White
    }

    val textColor = if (isMe) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = bubbleColor,
            tonalElevation = 1.dp,
            shadowElevation = 0.5.dp, // Soft shadow like in the image
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
            Column(modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )) {
                if (message.isDeleted) {
                    Text(
                        text = "This message was deleted",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    when (message.type) {
                        MessageType.TEXT -> {
                            Text(
                                text = message.content,
                                color = textColor,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        else -> { /* Handle Payments here later */ }
                    }

                    // Footer with Time and Status
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTimestamp(message.timestamp),
                            fontSize = 10.sp,
                            color = textColor.copy(alpha = 0.6f)
                        )

                        if (isMe) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (message.status == MessageStatus.SENT)
                                    Icons.Default.Check else Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(10.dp),
                                tint = textColor.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}