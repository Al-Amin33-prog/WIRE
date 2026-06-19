package com.example.wire.feature.auth.data.remote.authApiServices

import retrofit2.Response
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/sync-user")
    suspend fun syncUser(): Response<Unit>
}