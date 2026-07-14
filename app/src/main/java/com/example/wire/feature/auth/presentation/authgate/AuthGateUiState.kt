package com.example.wire.feature.auth.presentation.authgate



sealed interface AuthGateUiState {

    data object Loading : AuthGateUiState

    data object Authenticated : AuthGateUiState

    data object Unauthenticated : AuthGateUiState
}