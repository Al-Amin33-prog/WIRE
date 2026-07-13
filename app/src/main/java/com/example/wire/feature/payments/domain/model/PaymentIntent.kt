package com.example.wire.feature.payments.domain.model

data class PaymentIntent(
    val clientSecret: String,
    val publishableKey: String,
    val customerId: String? = null,      // Added
    val ephemeralKeySecret: String? = null // Added
)