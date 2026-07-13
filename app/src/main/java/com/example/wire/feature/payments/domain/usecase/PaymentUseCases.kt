package com.example.wire.feature.payments.domain.usecase



data class PaymentUseCases(
    val createIntent: CreatePaymentIntentUseCase,
    val confirmPayment: ConfirmPaymentUseCase

)