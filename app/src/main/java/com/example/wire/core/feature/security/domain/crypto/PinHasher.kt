package com.example.wire.core.feature.security.domain.crypto

interface PinHasher {
    fun hash(pin:String): String
    fun verify(
        pin: String,
        hashedPin: String
    ): Boolean
}