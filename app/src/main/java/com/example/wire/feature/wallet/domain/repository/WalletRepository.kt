package com.example.wire.feature.wallet.domain.repository

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.wallet.domain.model.Transaction
import com.example.wire.feature.wallet.domain.model.WalletBalance
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    suspend fun getBalance(): Resource<WalletBalance>


    suspend fun syncTransactions(): Resource<Unit>


    fun observeTransactions(): Flow<List<Transaction>>
}