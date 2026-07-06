package com.example.wire.feature.wallet.domain.usecase



import com.example.wire.core.common.util.Resource
import com.example.wire.feature.wallet.domain.repository.WalletRepository
import com.example.wire.feature.wallet.domain.model.WalletBalance
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): Resource<WalletBalance> {
        return repository.getBalance()
    }
}