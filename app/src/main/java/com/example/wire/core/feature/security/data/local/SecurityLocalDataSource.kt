package com.example.wire.core.feature.security.data.local

interface SecurityLocalDataSource {
    suspend fun  savePinHash(pinHash: String)
    suspend fun getPinHash(): String?
    suspend fun hasPin(): Boolean
    suspend fun setBiometricEnabled(enabled: Boolean)
    suspend fun isBiometricEnabled(): Boolean
    suspend fun clearSecurity()
    suspend fun setPendingSync(pending: Boolean)
    suspend fun isPendingSync(): Boolean
}