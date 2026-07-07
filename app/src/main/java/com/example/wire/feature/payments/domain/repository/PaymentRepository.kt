package com.example.wire.feature.payments.domain.repository


import com.example.wire.core.common.util.Resource
import com.example.wire.feature.payments.domain.model.PaymentIntent


interface PaymentRepository {
    // 1. Ask our backend to create a Stripe session
    suspend fun createPaymentIntent(
        amount: Double,
        recipientId: String,
        note: String?
    ): Resource<PaymentIntent>

    // 2. Update local DB and notify backend after Stripe is done
    suspend fun confirmPayment(
        paymentId: String,
        status: String
    ): Resource<Unit>

    // 3. Get specific payment status
    suspend fun getPaymentStatus(paymentId: String): Resource<String>
}