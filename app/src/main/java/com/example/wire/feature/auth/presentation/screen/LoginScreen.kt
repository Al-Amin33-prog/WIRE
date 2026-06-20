package com.example.wire.feature.auth.presentation.screen


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.util.LocalFragmentActivity
import com.example.wire.feature.auth.presentation.AuthViewModel
import com.example.wire.feature.auth.presentation.screen.content.LoginContent
import com.example.wire.feature.auth.presentation.state.AuthUiState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // 1. Get the activity directly from our CompositionLocal
    val activity = LocalFragmentActivity.current
    val uiState by viewModel.uiState.collectAsState()

    // 2. Trigger the prompt through the ViewModel (Manager is already injected there)
    LaunchedEffect(uiState.showBiometricPrompt) {
        if (uiState.showBiometricPrompt) {
            viewModel.showBiometricPrompt(activity)
        }
    }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}


@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    MaterialTheme {
        LoginContent(
            uiState = AuthUiState(
                isBiometricAvailable = true,
                isBiometricEnabled = true
            ),
            onEvent = {},
            onNavigateToSignUp = {},
            onNavigateToForgotPassword = {}
        )
    }
}
