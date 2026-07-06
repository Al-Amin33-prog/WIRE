package com.example.wire.feature.wallet.data.mapper

import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.feature.wallet.data.remote.WalletBalanceDto
import com.example.wire.feature.wallet.data.remote.TransactionDto
import com.example.wire.feature.wallet.domain.model.Transaction
import com.example.wire.feature.wallet.domain.model.TransactionType
import com.example.wire.feature.wallet.domain.model.WalletBalance

/**
 * Converts API Response to UI Domain Model
 */
fun WalletBalanceDto.toDomain() = WalletBalance(
    amount = amount,
    currency = currency
)

/**
 * Converts API Response to Database Entity (SSOT)
 */
fun TransactionDto.toEntity() = TransactionEntity(
    id = id,
    amount = amount,
    type = type, // Both are Strings
    status = status,
    timestamp = timestamp,
    note = note,
    counterPartyName = senderName,
    // FIX: Map senderId from DTO to counterPartyId in Entity
    counterPartyId = senderId
)

/**
 * Converts Database Entity to UI Domain Model
 */
fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amount = amount,
    // FIX: Convert String from DB to the TransactionType Enum
    type = try {
        TransactionType.valueOf(type)
    } catch (e: Exception) {
        TransactionType.SEND // Fallback
    },
    status = status,
    timestamp = timestamp,
    note = note,
    counterPartyName = counterPartyName
)