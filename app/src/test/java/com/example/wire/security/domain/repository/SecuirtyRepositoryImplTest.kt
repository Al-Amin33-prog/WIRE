package com.example.wire.security.domain.repository



import com.example.wire.core.feature.security.data.crypto.Sha256PinHasher
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import com.example.wire.core.feature.security.data.remote.SecurityRemoteDataSource
import com.example.wire.core.feature.security.data.repository.SecurityRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SecurityRepositoryImplTest {

    private lateinit var repository: SecurityRepositoryImpl
    private val localDataSource: SecurityLocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: SecurityRemoteDataSource = mockk(relaxed = true)
    private val pinHasher: Sha256PinHasher = mockk()

    @Before
    fun setup() {
        repository = SecurityRepositoryImpl(localDataSource, pinHasher, remoteDataSource)
    }

    @Test
    fun `createPin should hash pin and save to both local and remote`() = runTest {
        val rawPin = "1234"
        val hashedPin = "hashed_1234"

        every { pinHasher.hash(rawPin) } returns hashedPin

        repository.createPin(rawPin)

        coVerify { localDataSource.savePinHash(hashedPin) }
        coVerify { remoteDataSource.uploadPinHash(hashedPin) }
    }

    @Test
    fun `verifyPin should return true if hashed input matches stored hash`() = runTest {
        val inputPin = "1234"
        val storedHash = "stored_hash"

        coEvery { localDataSource.getPinHash() } returns storedHash
        every { pinHasher.verify(inputPin, storedHash) } returns true

        val result = repository.verifyPin(inputPin)

        assertTrue(result)
    }

    @Test
    fun `getSecuritySettings should fallback to local if remote fails`() = runTest {
        // Force remote to fail
        coEvery { remoteDataSource.getSecuritySettings() } throws Exception("Network Error")

        // Setup local data
        coEvery { localDataSource.hasPin() } returns true
        coEvery { localDataSource.isBiometricEnabled() } returns false

        val settings = repository.getSecuritySettings()

        assertEquals(true, settings.hasPin)
        assertEquals(false, settings.isBiometricEnabled)
        coVerify { localDataSource.hasPin() }
    }

    @Test
    fun `enableBiometric should update both local and remote`() = runTest {
        repository.enableBiometric()

        coVerify { localDataSource.setBiometricEnabled(true) }
        coVerify { remoteDataSource.updateBiometricStatus(true) }
    }
}