package com.example.wire.payment.util



import com.example.wire.feature.payments.data.util.IdempotencyKeyGeneratorImpl
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class IdempotencyKeyGeneratorTest {

    private val generator = IdempotencyKeyGeneratorImpl()

    @Test
    fun `generate should return non-null unique UUIDs`() {
        val key1 = generator.generate()
        val key2 = generator.generate()

        assertNotNull(key1)
        assertNotNull(key2)
        // Ensure no two keys are identical (Crucial for Fintech)
        assertNotEquals(key1, key2)
    }
}