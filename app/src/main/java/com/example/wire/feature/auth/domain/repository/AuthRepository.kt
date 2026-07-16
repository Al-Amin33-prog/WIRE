package com.example.wire.feature.auth.domain.repository

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.auth.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Resource<AuthUser>

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone: String
    ): Resource<AuthUser>

    suspend fun logout()

    fun observeAuthState(): Flow<AuthUser?>

    suspend fun getCurrentUser(): AuthUser?

    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>
    suspend fun loginWithGoogle(idToken: String): Resource<AuthUser>
    suspend fun syncUser(): Resource<Unit>
}