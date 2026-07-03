package com.example.wire.feature.auth.data.remote.authApiServices

import com.example.wire.feature.auth.data.remote.dto.AuthUserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/sync-user")
    suspend fun syncUser(): Response<Unit>

    // In AuthApiService.kt
    @POST("auth/sync")
    suspend fun syncUser(@Body user: AuthUserDto): Response<Unit>
}