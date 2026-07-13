package com.example.wire.feature.payments.domain.model

sealed class PaymentResult {
    object Success : PaymentResult()
    data class Failure(val message: String) : PaymentResult()
    object Cancelled : PaymentResult()
}