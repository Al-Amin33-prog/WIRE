package com.example.wire.feature.wallet.presentation.screen



import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.feature.wallet.presentation.WalletViewModel
import com.example.wire.feature.wallet.presentation.state.SavingsGoal
import com.example.wire.feature.wallet.presentation.state.WalletUiState


@Composable
fun WalletScreen(
    onBackClick: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    WireTheme {
        WalletContent(
            uiState = uiState,
            onRefresh = { viewModel.onEvent(com.example.wire.feature.wallet.presentation.event.WalletUiEvent.Refresh) },
            onBackClick = onBackClick
        )
    }


}


@Preview(showBackground = true, name = "Light Mode")
@Composable
fun WalletPreviewLight() {
    com.example.wire.core.ui.theme.WireTheme(darkTheme = false) {
        WalletContent(
            uiState = WalletUiState(
                savingGoals = listOf(
                    SavingsGoal("Summer Vacation", 1360.0, 2000.0, "🏖️"),
                    SavingsGoal("New MacBook", 600.0, 1500.0, "💻")
                )
            ),
            onRefresh = {},
            onBackClick = {}
        )
    }
}