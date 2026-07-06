package com.example.wire.feature.wallet.domain.model



data class WalletBalance(
    val amount: Double,
    val currency: String = "USD"
)

data class Transaction(
    val id: String,
    val amount: Double,
    val type: TransactionType,
    val status: String,
    val timestamp: Long,
    val note: String?,
    val counterPartyName: String
)

enum class TransactionType {
    SEND, RECEIVE, TOP_UP
}