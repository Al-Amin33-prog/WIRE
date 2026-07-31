package com.example.wire.feature.auth.data.remote.authApiServices


import com.example.wire.feature.auth.data.remote.dto.ForgotPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/sync-user")
    suspend fun syncUser(): Response<Unit>
    @POST("api/auth/forgot-password")
    suspend fun forgotPasswordRequest(
        @Body request: ForgotPasswordRequest
    ): Response<Unit>
}