package com.example.wire.feature.payments.data.mapper

import com.example.wire.core.database.entity.TransactionEntity
import com.example.wire.feature.chat.data.remote.dto.MessageDto

fun MessageDto.toTransactionEntity(status: String = "COMPLETED"): TransactionEntity {
    return TransactionEntity(
        id = this.id  ,
        amount = this.content.toDoubleOrNull() ?: 0.0,
        type = "RECEIVE",
        status = status,
        timestamp = this.timestamp,
        note = this.metadata?.get("note"),
        counterPartyName = this.metadata?.get("senderName") ?: "Unknown Sender",
        counterPartyId = this.senderId
    )
}