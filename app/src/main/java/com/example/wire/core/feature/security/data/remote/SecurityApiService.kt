package com.example.wire.core.feature.security.data.remote

import com.example.wire.core.feature.security.data.remote.dto.RemoteSecuritySettings
import com.example.wire.core.feature.security.data.remote.dto.UpdateBiometricRequest
import com.example.wire.core.feature.security.data.remote.dto.UploadPinRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface SecurityApiService {
    @POST("api/security/pin")
    suspend fun uploadPin(
        @Body request: UploadPinRequest
    )
    @PATCH("api/security/biometric")
    suspend fun updateBiometric(
        @Body request: UpdateBiometricRequest
    )
    @GET("api/security/settings")
    suspend fun getSecuritySettings(): RemoteSecuritySettings

}