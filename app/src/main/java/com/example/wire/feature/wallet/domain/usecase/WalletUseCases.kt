package com.example.wire.feature.wallet.domain.usecase



data class WalletUseCases(
    val getBalance: GetBalanceUseCase,
    val syncTransactions: SyncTransactionsUseCase,
    val observeTransactions: ObserveTransactionsUseCase
)