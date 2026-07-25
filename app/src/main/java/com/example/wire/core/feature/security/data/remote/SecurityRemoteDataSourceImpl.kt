package com.example.wire.core.feature.security.data.remote

import com.example.wire.core.feature.security.data.remote.dto.RemoteSecuritySettings
import com.example.wire.core.feature.security.data.remote.dto.UpdateBiometricRequest
import com.example.wire.core.feature.security.data.remote.dto.UploadPinRequest
import javax.inject.Inject

class SecurityRemoteDataSourceImpl @Inject constructor(
    private val api: SecurityApiService,
) : SecurityRemoteDataSource{

    override suspend fun uploadPinHash(
        pinHash: String
    ) {
       api.uploadPin(
           UploadPinRequest(
               pinHash = pinHash
           )
       )

    }

    override suspend fun updateBiometricStatus(
        enabled: Boolean
    ) {
      api.updateBiometric(
          UpdateBiometricRequest(
              biometricEnabled = enabled
          )
      )
    }

    override suspend fun getSecuritySettings(): RemoteSecuritySettings {
        return api.getSecuritySettings()
    }
}