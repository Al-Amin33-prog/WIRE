package com.example.wire.feature.wallet.presentation.state



import com.example.wire.feature.wallet.domain.model.Transaction
import com.example.wire.feature.wallet.domain.model.WalletBalance

data class SavingsGoal(
    val title: String,
    val savedAmount: Double,
    val targetAmount: Double,
    val iconEmoji: String
)

data class WalletUiState(
    val balance: WalletBalance? = null,
    val savingGoals: List<SavingsGoal> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)