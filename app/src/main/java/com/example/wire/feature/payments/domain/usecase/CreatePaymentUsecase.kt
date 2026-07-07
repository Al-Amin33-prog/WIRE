package com.example.wire.feature.payments.domain.usecase

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.payments.domain.model.PaymentIntent
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class CreatePaymentIntentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(amount: Double, recipientId: String, note: String?): Resource<PaymentIntent> {
        if (amount <= 0) return Resource.Error(AppError.Validation("Amount must be greater than zero"))
        return repository.createPaymentIntent(amount, recipientId, note)
    }
}