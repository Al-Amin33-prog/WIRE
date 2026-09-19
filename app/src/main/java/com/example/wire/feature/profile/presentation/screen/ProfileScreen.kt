package com.example.wire.feature.profile.presentation.screen

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.feature.profile.presentation.ProfileViewModel
import com.example.wire.feature.profile.presentation.screen.content.ProfileContent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate to log in when signed out or account deleted
    LaunchedEffect(uiState.isSignedOut, uiState.isAccountDeleted) {
        if (uiState.isSignedOut || uiState.isAccountDeleted) {
            onNavigateToLogin()
        }
    }

    ProfileContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
