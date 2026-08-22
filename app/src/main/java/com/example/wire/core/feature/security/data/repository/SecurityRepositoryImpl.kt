package com.example.wire.core.feature.security.data.repository

import com.example.wire.core.feature.security.data.crypto.Sha256PinHasher
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import com.example.wire.core.feature.security.data.remote.SecurityRemoteDataSource
import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject
import java.io.IOException // Import this for network errors

class SecurityRepositoryImpl @Inject constructor(
    private val localDataSource: SecurityLocalDataSource,
    private val pinHasher: Sha256PinHasher,
    private val remoteDataSource: SecurityRemoteDataSource
): SecurityRepository {

    override suspend fun verifyPin(pin: String): Boolean {
        val storedHash = localDataSource.getPinHash() ?: return false
        return pinHasher.verify(pin, storedHash)
    }

    override suspend fun enableBiometric() {
        localDataSource.setBiometricEnabled(true)
        try {
            remoteDataSource.updateBiometricStatus(true)
        } catch (e: Exception) {
            // Silently fail or log - prevents crash if network is down
            e.printStackTrace()
        }
    }

    override suspend fun disableBiometric() {
        localDataSource.setBiometricEnabled(false)
        try {
            remoteDataSource.updateBiometricStatus(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getSecuritySettings(): SecuritySettings {
        return try {
            val remote = remoteDataSource.getSecuritySettings()
            SecuritySettings(
                hasPin = remote.hasPin,
                isBiometricEnabled = remote.biometricEnabled
            )
        } catch (e: Exception) {
            // This one you already had - it falls back to local data correctly
            SecuritySettings(
                hasPin = localDataSource.hasPin(),
                isBiometricEnabled = localDataSource.isBiometricEnabled()
            )
        }
    }

    override suspend fun createPin(pin: String) {
        val hashedPin = pinHasher.hash(pin)
        // 1. Always save locally first so the user isn't locked out
        localDataSource.savePinHash(hashedPin)

        // 2. Wrap the remote call in try-catch to prevent FATAL EXCEPTION
        try {
            remoteDataSource.uploadPinHash(hashedPin)
        } catch (e: Exception) {
            // If the ADB tunnel is down or server is off, we don't crash.
            // We just log the error. The user still has their PIN saved locally.
            e.printStackTrace()
        }
    }

    override suspend fun hasPin(): Boolean {
        return localDataSource.hasPin()
    }
}