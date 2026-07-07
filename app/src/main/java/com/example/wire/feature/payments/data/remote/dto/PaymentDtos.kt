package com.example.wire.feature.payments.data.remote.dto



import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentRequest(
    val amount: Double,
    val recipientId: String,
    val note: String?,
    val idempotencyKey: String
)

@Serializable
data class PaymentIntentResponse(
    val clientSecret: String,
    val publishableKey: String,
    val paymentId: String,
    val idempotencyKey: String
)

@Serializable
data class ConfirmPaymentRequest(
    val paymentId: String,
    val status: String ,
    val note: String? = null,

)

@Serializable
data class PaymentActionDto(
    val action: String, // "SEND_PAYMENT", "REQUEST_PAYMENT", "CONFIRM_REQUEST"
    val paymentId: String? = null,
    val amount: Double,
    val note: String? = null,
    val recipientId: String,
    val requesterId: String? = null
)
