package com.example.wire.feature.wallet.domain.usecase



import com.example.wire.feature.wallet.domain.repository.WalletRepository
import com.example.wire.feature.wallet.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionsUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.observeTransactions()
    }
}