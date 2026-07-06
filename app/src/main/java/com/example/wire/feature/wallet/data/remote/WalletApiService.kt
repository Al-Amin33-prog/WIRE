package com.example.wire.feature.wallet.data.remote



import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class WalletBalanceDto(
    val amount: Double,
    val currency: String
)

@Serializable
data class TransactionDto(
    val id: String,
    val amount: Double,
    val type: String, // "SEND", "RECEIVE", "TOP_UP"
    val status: String, // "COMPLETED", "PENDING"
    val timestamp: Long,
    val senderName: String,
    val senderId: String,
    val note: String? = null
)

interface WalletApiService {
    @GET("api/wallet/balance")
    suspend fun getBalance(): WalletBalanceDto

    @GET("api/wallet/transactions")
    suspend fun getTransactionHistory(): List<TransactionDto>
}