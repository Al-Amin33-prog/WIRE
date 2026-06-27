package com.example.wire.feature.notifications.presentation.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wire.feature.notifications.presentation.component.NotificationItem
import com.example.wire.feature.notifications.presentation.event.NotificationUIEvent
import com.example.wire.feature.notifications.presentation.state.NotificationUIState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationUIState,
    onEvent: (NotificationUIEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity",
                    fontWeight = FontWeight.Bold)
                        },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.notifications.isNotEmpty()) {
                        TextButton(onClick = {
                            onEvent(NotificationUIEvent.ClearAll)
                        }) {
                            Text("Clear All")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (uiState.notifications.isEmpty() && !uiState.isLoading) {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(
                        "No recent activity",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = uiState.notifications,
                    key = { it.id }
                ) { notification ->
                    NotificationItem(
                        notification = notification,
                        onClick = {
                            onEvent(NotificationUIEvent.MarkAsRead(notification.id))
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
