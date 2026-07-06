package com.example.wire.feature.wallet.data.repository

import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.common.util.Resource
import com.example.wire.core.common.util.AppError
import com.example.wire.core.database.dao.TransactionDao
import com.example.wire.feature.wallet.data.remote.WalletApiService
import com.example.wire.feature.wallet.data.mapper.toDomain
import com.example.wire.feature.wallet.data.mapper.toEntity
import com.example.wire.feature.wallet.domain.model.WalletBalance
import com.example.wire.feature.wallet.domain.model.Transaction
import com.example.wire.feature.wallet.domain.repository.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val api: WalletApiService,
    private val transactionDao: TransactionDao,
    private val performanceMonitor: PerformanceMonitor
) : WalletRepository {

    override suspend fun getBalance(): Resource<WalletBalance> = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            val response = api.getBalance()
            performanceMonitor.recordEvent(System.currentTimeMillis() - startTime)
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override fun observeTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncTransactions(): Resource<Unit> = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            val dtos = api.getTransactionHistory()
            val entities = dtos.map { it.toEntity() }

            transactionDao.upsertTransactions(entities)

            performanceMonitor.recordEvent(System.currentTimeMillis() - startTime)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}