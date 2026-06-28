package com.example.wire.feature.chat.presentation.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HeaderSection(
    userName: String,
    unreadCount: Int = 3,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good morning, $userName 👋",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Messages",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Search Icon
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Notification Icon with Official Material 3 Badging
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        CircleShape)
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                modifier = Modifier.size(16.dp)
                            ) {
                                Text(unreadCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Activity",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}