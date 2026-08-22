package com.example.wire.feature.profile.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wire.feature.profile.presentation.ProfileViewModel
import com.example.wire.feature.profile.presentation.screen.content.ProfileContent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        state = state,
        onEvent = viewModel::onEvent,
        onEditProfile = onEditProfile,
        onNavigateToSettings = onNavigateToSettings
    )
}