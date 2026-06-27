package com.example.wire.feature.auth.presentation.screen


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.feature.auth.presentation.AuthViewModel
import com.example.wire.feature.auth.presentation.screen.content.ForgotPasswordContent
import com.example.wire.feature.auth.presentation.state.AuthUiState

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Preview(showBackground = true)
@Composable
private fun ForgotPasswordPreview() {
    MaterialTheme {
        ForgotPasswordContent(
            uiState = AuthUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
