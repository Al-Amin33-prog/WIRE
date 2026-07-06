package com.example.wire.feature.wallet.domain.usecase



import com.example.wire.core.common.util.Resource
import com.example.wire.feature.wallet.domain.repository.WalletRepository
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.syncTransactions()
    }
}