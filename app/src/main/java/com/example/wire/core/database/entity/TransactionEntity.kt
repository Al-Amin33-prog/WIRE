package com.example.wire.core.database.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val type: String, // SEND, RECEIVE
    val status: String, // COMPLETED, PENDING, FAILED
    val timestamp: Long,
    val note: String?,
    val counterPartyName: String ,
    val counterPartyId: String
)