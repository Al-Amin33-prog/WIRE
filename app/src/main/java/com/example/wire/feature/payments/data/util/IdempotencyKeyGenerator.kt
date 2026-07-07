package com.example.wire.feature.payments.data.util



import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface IdempotencyKeyGenerator {
    fun generate(): String
}

@Singleton
class IdempotencyKeyGeneratorImpl @Inject constructor() : IdempotencyKeyGenerator {
    override fun generate(): String {
        // Standard UUID v4 is perfect for idempotency keys
        return UUID.randomUUID().toString()
    }
}