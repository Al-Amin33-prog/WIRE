package com.example.wire.core.feature.security.data.crypto

import com.example.wire.core.feature.security.domain.crypto.PinHasher
import java.security.MessageDigest
import javax.inject.Inject

class Sha256PinHasher @Inject constructor(): PinHasher {
    override fun hash(pin: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(pin.toByteArray())
        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

    override fun verify(pin: String, hashedPin: String): Boolean {
        return hash(pin) == hashedPin
    }
}