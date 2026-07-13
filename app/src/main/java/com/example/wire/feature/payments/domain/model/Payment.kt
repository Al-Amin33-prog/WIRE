package com.example.wire.feature.payments.domain.model



data class Payment(
    val id: String,
    val amount: Double,
    val currency: String = "USD",
    val recipientId: String,
    val note: String?,
    val timestamp: Long,
    val status: PaymentStatus
)





