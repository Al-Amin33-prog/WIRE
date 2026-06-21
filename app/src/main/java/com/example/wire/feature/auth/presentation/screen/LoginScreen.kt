package com.example.wire.feature.auth.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.R
import com.example.wire.core.ui.util.LocalFragmentActivity
import com.example.wire.feature.auth.presentation.AuthViewModel
import com.example.wire.feature.auth.presentation.event.AuthUiEvent
import com.example.wire.feature.auth.presentation.screen.content.LoginContent
import com.example.wire.feature.auth.presentation.state.AuthUiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalFragmentActivity.current
    val uiState by viewModel.uiState.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.onEvent(AuthUiEvent.GoogleSignInResult(idToken))
            } else {
                viewModel.onEvent(AuthUiEvent.GoogleSignInFailed("No ID token received"))
            }
        } catch (e: ApiException) {
            viewModel.onEvent(AuthUiEvent.GoogleSignInFailed(e.message ?: "Google sign-in failed"))
        }
    }

    LaunchedEffect(uiState.triggerGoogleSignIn) {
        if (uiState.triggerGoogleSignIn) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

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
