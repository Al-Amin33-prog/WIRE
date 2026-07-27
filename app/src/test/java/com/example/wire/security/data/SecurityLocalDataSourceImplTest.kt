package com.example.wire.security.data



import com.example.wire.core.feature.security.data.datastore.SecurityPreferences
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SecurityLocalDataSourceImplTest {

    private lateinit var dataSource: SecurityLocalDataSourceImpl
    private val securityPreferences: SecurityPreferences = mockk(relaxed = true)

    @Before
    fun setup() {
        dataSource = SecurityLocalDataSourceImpl(securityPreferences)
    }

    @Test
    fun `savePinHash should call preferences to save hash`() = runTest {
        val testHash = "hashed_pin_123"
        dataSource.savePinHash(testHash)
        coVerify { securityPreferences.savePinHash(testHash) }
    }

    @Test
    fun `hasPin should return true when hash exists in preferences`() = runTest {
        coEvery { securityPreferences.pinHash } returns flowOf("some_hash")

        val result = dataSource.hasPin()

        assertTrue(result)
    }

    @Test
    fun `isBiometricEnabled should return correct state from preferences`() = runTest {
        coEvery { securityPreferences.biometricEnabled } returns flowOf(true)

        val result = dataSource.isBiometricEnabled()

        assertEquals(true, result)
    }
}