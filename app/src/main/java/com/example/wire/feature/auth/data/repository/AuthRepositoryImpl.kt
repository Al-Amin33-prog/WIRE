package com.example.wire.feature.auth.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.auth.data.mapper.toDomain
import com.example.wire.feature.auth.data.remote.FirebaseAuthDataSource
import com.example.wire.feature.auth.data.remote.authApiServices.AuthApiService
import com.example.wire.feature.auth.data.remote.dto.ForgotPasswordRequest
import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<AuthUser> {
        return try {
            val user = firebaseAuthDataSource.login(email,password)
            return  Resource.Success(user.toDomain())


        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String,
        phone: String
    ): Resource<AuthUser> {
        return try {
            val userDto = firebaseAuthDataSource.register(
                email,
                password,
                displayName,
                phone
            )


            val response = authApiService.syncUser()
            if (response.isSuccessful){
                 Resource.Success(userDto.toDomain())

            } else {
                Resource.Error(AppError.Network.ServerError)
            }

        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
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

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            val response = authApiService.forgotPasswordRequest(
                ForgotPasswordRequest(email)
            )
            if (response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error(
                    AppError.Network.ServerError
                )
            }
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        }catch (e:Exception){
            Resource.Error(
                AppError.Network.Unknown(e.message)
            )
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Resource<AuthUser> {
        return try {
            val userDto = firebaseAuthDataSource.loginWithGoogle(idToken)
            authApiService.syncUser()
            Resource.Success(userDto.toDomain())
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun syncUser(): Resource<Unit> {
        return try{
            authApiService.syncUser()
            Resource.Success(Unit)
        }catch (e: Exception){
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }
}