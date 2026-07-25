package com.example.wire.core.feature.security.data.remote

import com.example.wire.core.feature.security.data.remote.dto.RemoteSecuritySettings

interface SecurityRemoteDataSource{
    suspend fun uploadPinHash(
        pinHash: String
    )
    suspend fun updateBiometricStatus(
        enabled: Boolean
    )
    suspend fun getSecuritySettings(): RemoteSecuritySettings
}