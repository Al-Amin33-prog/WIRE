package com.example.wire.feature.notifications.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.notifications.presentation.NotificationsViewModel
import com.example.wire.feature.notifications.presentation.state.NotificationUIState

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    WireTheme {
        NotificationsContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onBackClick = onBackClick
        )
    }


}

@Preview
@Composable
fun NotificationScreenPreview(){
    WireTheme(darkTheme = true) {
        NotificationsContent(
            uiState = NotificationUIState(
                isLoading = false,
                error = ""

            ),
            onEvent = {},
            onBackClick = {}
        )
    }

}