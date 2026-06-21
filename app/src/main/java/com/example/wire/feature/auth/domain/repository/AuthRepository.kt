package com.example.wire.feature.auth.domain.repository

import com.example.wire.feature.auth.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthUser>

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone: String
    ): Result<AuthUser>

    suspend fun logout()

    fun observeAuthState(): Flow<AuthUser?>

    suspend fun getCurrentUser(): AuthUser?

    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}