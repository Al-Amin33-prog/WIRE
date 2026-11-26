package com.example.wire.feature.profile.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.UserDao
import com.example.wire.core.database.entity.UserEntity
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.core.feature.security.data.datastore.SecurityPreferences
import com.example.wire.feature.profile.data.mapper.toDomain
import com.example.wire.feature.profile.data.mapper.toLocalProfile
import com.example.wire.feature.profile.data.remote.ProfileApiService
import com.example.wire.feature.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService,
    private val userDao: UserDao,
    private val securityPreferences: SecurityPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers
) : ProfileRepository {

    override fun observeProfile(): Flow<Profile?> {
        return userDao.observeUserProfile().combine(securityPreferences.biometricEnabled) { entity, biometric ->
            entity?.toDomain(isBiometricEnabled = biometric)
                ?: firebaseAuth.currentUser?.toLocalProfile()
        }
    }

    override suspend fun getProfile(): Resource<Profile> = withContext(dispatchers.io) {
        try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = UserEntity(
                    id = dto.uid,
                    fullName = dto.displayName,
                    username = dto.userName,
                    email = dto.email,
                    phoneNumber = dto.phoneNumber,
                    bio = dto.bio,
                    avatarUrl = dto.avatarUrl,
                    isVerified = dto.isKycVerified,
                    joinedAt = dto.joinedAt
                )
                userDao.upsertUserProfile(entity)
                Resource.Success(dto.toDomain())
            } else {
                val local = userDao.getUserProfile()
                if (local != null) {
                    Resource.Success(local.toDomain())
                } else {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        Resource.Success(firebaseUser.toLocalProfile())
                    } else {
                        Resource.Error(AppError.Network.ServerError)
                    }
                }
            }
        } catch (e: IOException) {
            val local = userDao.getUserProfile()
            if (local != null) {
                Resource.Success(local.toDomain())
            } else {
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    Resource.Success(firebaseUser.toLocalProfile())
                } else {
                    Resource.Error(AppError.Network.NoInternet)
                }
            }
        } catch (e: Exception) {
            val local = userDao.getUserProfile()
            if (local != null) {
                Resource.Success(local.toDomain())
            } else {
                Resource.Error(AppError.Network.Unknown(e.message))
            }
        }
    }

    override suspend fun updateProfile(profile: Profile): Resource<Profile> = withContext(dispatchers.io) {
        try {
            val request = UpdateProfileRequestDto(
                displayName = profile.displayName,
                bio = profile.bio
            )
            val response = apiService.updateProfile(request)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val entity = UserEntity(
                    id = dto.uid,
                    fullName = dto.displayName,
                    username = dto.userName,
                    email = dto.email,
                    phoneNumber = dto.phoneNumber,
                    bio = dto.bio,
                    avatarUrl = dto.avatarUrl,
                    isVerified = dto.isKycVerified,
                    joinedAt = dto.joinedAt
                )
                userDao.upsertUserProfile(entity)
                Resource.Success(dto.toDomain())
            } else {
                Resource.Error(AppError.Network.ServerError)
            }
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun uploadAvatar(avatar: ByteArray): Resource<String> = withContext(dispatchers.io) {
        try {
            val requestFile = avatar.toRequestBody("image/*".toMediaTypeOrNull(), 0, avatar.size)
            val body = MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestFile)
            val response = apiService.uploadAvatar(body)
            if (response.isSuccessful && response.body() != null) {
                val url = response.body()!!["url"] ?: ""
                Resource.Success(url)
            } else {
                Resource.Error(AppError.Network.ServerError)
            }
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun updateDisplayName(name: String): Resource<String> = withContext(dispatchers.io) {
        try {
            val response = apiService.updateProfile(UpdateProfileRequestDto(displayName = name))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val local = userDao.getUserProfile()
                if (local != null) {
                    userDao.upsertUserProfile(local.copy(fullName = name))
                }
                Resource.Success(dto.displayName)
            } else {
                Resource.Error(AppError.Network.ServerError)
            }
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun updateBio(bio: String): Resource<Unit> = withContext(dispatchers.io) {
        try {
            val response = apiService.updateProfile(UpdateProfileRequestDto(bio = bio))
            if (response.isSuccessful) {
                val local = userDao.getUserProfile()
                if (local != null) {
                    userDao.upsertUserProfile(local.copy(bio = bio))
                }
                Resource.Success(Unit)
            } else {
                Resource.Error(AppError.Network.ServerError)
            }
        } catch (e: IOException) {
            Resource.Error(AppError.Network.NoInternet)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun updateBiometricEnabled(enabled: Boolean): Resource<Unit> = withContext(dispatchers.io) {
        try {
            securityPreferences.setBiometricEnabled(enabled)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun updatePaymentPinEnabled(enabled: Boolean): Resource<Unit> = withContext(dispatchers.io) {
        try {
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun updatePushNotificationsEnabled(enabled: Boolean): Resource<Unit> = withContext(dispatchers.io) {
        try {
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Network.Unknown(e.message))
        }
    }

    override suspend fun deleteAccount(): Result<Unit> = withContext(dispatchers.io) {
        try {
            val response = apiService.deleteAccount()
            if (response.isSuccessful) {
                firebaseAuth.currentUser?.delete()
                userDao.clearUserProfile()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete account"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() = withContext(dispatchers.io) {
        try {
            firebaseAuth.signOut()
            userDao.clearUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
