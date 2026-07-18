package com.example.wire.core.feature.security.data.repository

import com.example.wire.core.feature.security.data.crypto.Sha256PinHasher
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import com.example.wire.core.feature.security.domain.model.SecuritySettings
import com.example.wire.core.feature.security.domain.repository.SecurityRepository
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val localDataSource: SecurityLocalDataSource,
    private val pinHasher: Sha256PinHasher
): SecurityRepository{
    override suspend fun verifyPin(pin: String): Boolean {
       val storedHash = localDataSource.getPinHash()?: return false
        return pinHasher.verify(
            pin,
            storedHash
        )
    }

    override suspend fun enableBiometric() {
        localDataSource.setBiometricEnabled(true)
    }

    override suspend fun disableBiometric() {
        localDataSource.setBiometricEnabled(false)
    }

    override suspend fun getSecuritySettings(): SecuritySettings {
        return SecuritySettings(
            hasPin = localDataSource.hasPin(),
            isBiometricEnabled = localDataSource.isBiometricEnabled()
        )
    }

    override suspend fun createPin(pin: String) {
        val hashedPin = pinHasher.hash(pin)
        localDataSource.savePinHash(hashedPin)
    }

    override suspend fun hasPin(): Boolean {
        return localDataSource.hasPin()
    }

}