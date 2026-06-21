package com.example.wire.feature.auth.data.repository

import com.example.wire.feature.auth.data.mapper.toDomain
import com.example.wire.feature.auth.data.remote.FirebaseAuthDataSource
import com.example.wire.feature.auth.data.remote.authApiServices.AuthApiService
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        return try {
            val userDto = firebaseAuthDataSource.login(email, password)
            authApiService.syncUser()
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone:String
    ): Result<AuthUser> {
        return try {
            val userDto = firebaseAuthDataSource.register(email, password, displayName,phone)
            authApiService.syncUser()
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuthDataSource.logout()
    }

    override fun observeAuthState(): Flow<AuthUser?> {
        return firebaseAuthDataSource.observeAuthState().map { it?.toDomain() }
    }

    override suspend fun getCurrentUser(): AuthUser? {
        return firebaseAuthDataSource.getCurrentUser()?.toDomain()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuthDataSource.sendPasswordResetEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun loginWithGoogle(idToken: String): Result<AuthUser> {
        return try {
            val userDto = firebaseAuthDataSource.loginWithGoogle(idToken)
            authApiService.syncUser()
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}