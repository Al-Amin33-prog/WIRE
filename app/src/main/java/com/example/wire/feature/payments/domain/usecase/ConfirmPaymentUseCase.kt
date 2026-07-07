package com.example.wire.feature.payments.domain.usecase

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class ConfirmPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(paymentId: String, status: String): Resource<Unit> {
        return repository.confirmPayment(paymentId, status)
    }
}