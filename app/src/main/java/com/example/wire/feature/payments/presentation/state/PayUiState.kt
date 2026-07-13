package com.example.wire.feature.payments.presentation.state

import com.example.wire.feature.payments.domain.model.PaymentIntent


enum class PaymentTab { KEYPAD, QR_CODE }
enum class PaymentMode{ SEND, REQUEST}

data class PaymentUiState(
    val amount: String = "",
    val note: String = "",
    val manualHandleEntry: String = "",
    val paymentMode: PaymentMode = PaymentMode.SEND,
    val recipientName: String = "", // Dynamic: From the person you clicked
    val recipientId: String = "",
    val currentUserHandle: String = "", // Dynamic: Your own handle
    val selectedTab: PaymentTab = PaymentTab.KEYPAD, // For the toggle
    val isLoading: Boolean = false,
    val showConfirmSheet: Boolean = false,
    val error: String? = null,
    val isPaymentSuccessful: Boolean = false,
    val stripeClientSecret: String? = null,
    val stripePublishableKey: String? = null,
    val stripeCustomerId: String? = null,
    val stripeEphemeralKey: String? = null,
    val paymentIntent: PaymentIntent? = null

)
