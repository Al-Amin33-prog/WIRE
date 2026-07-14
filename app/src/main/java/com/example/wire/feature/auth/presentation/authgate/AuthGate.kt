package com.example.wire.feature.auth.presentation.authgate



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wire.feature.auth.presentation.AuthGateViewModel

@Composable
fun AuthGate(

    onAuthenticated: () -> Unit,

    onUnauthenticated: () -> Unit,

    viewModel: AuthGateViewModel = hiltViewModel()

) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {

        when (state) {

            AuthGateUiState.Authenticated ->
                onAuthenticated()

            AuthGateUiState.Unauthenticated ->
                onUnauthenticated()

            AuthGateUiState.Loading -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator()

    }
}