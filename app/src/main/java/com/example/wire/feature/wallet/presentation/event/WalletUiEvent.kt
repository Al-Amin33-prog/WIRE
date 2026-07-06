package com.example.wire.feature.wallet.presentation.event



sealed class WalletUiEvent {
    object Refresh : WalletUiEvent()
    data class LoadTransactionDetails(val transactionId: String) : WalletUiEvent()
}