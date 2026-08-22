package com.example.wire.feature.profile.data.repository

import com.example.wire.core.common.util.AppError
import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.UserDao
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.feature.profile.data.mapper.toDomain
import com.example.wire.feature.profile.data.mapper.toDto
import com.example.wire.feature.profile.data.mapper.toEntity
import com.example.wire.feature.profile.data.mapper.toUpdateDto
import com.example.wire.feature.profile.data.remote.ProfileApiService
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService,
    private val userDao: UserDao,
    private val dispatchers: CoroutineDispatchers
): ProfileRepository {
    override suspend fun observeProfile(): Flow<Profile?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(): Resource<Profile> = withContext(dispatchers.io) {
        try {
            // 1. Fetch from network
            val profileDto = apiService.getProfile()

            // 2. Update local DB (SSOT)
            userDao.upsertUserProfile(profileDto.toEntity())

            Resource.Success(profileDto.toDomain())
        } catch (e: Exception) {
            // 3. Fallback: If network fails, check local DB
            val localEntity = userDao.getUserProfile()
            if (localEntity != null) {
                Resource.Success(localEntity.toDomain())
            } else {
                // FIXED: Wrapped String in AppError.Network.Unknown
                Resource.Error(AppError.Network.Unknown(e.message ?: "No internet and no local data"))
            }
        }
    }

    override suspend fun updateProfile(profile: Profile): Resource<Profile> = withContext(dispatchers.io) {
        try {
            // FIXED: Ensure we are passing the correct DTO type required by your ApiService
            val profileDto = apiService.updateProfile(profile.toUpdateDto())

            // Sync the updated profile back to local Room
            userDao.upsertUserProfile(profileDto.toEntity())

            Resource.Success(profileDto.toDomain())
        } catch (e: Exception) {
            // FIXED: Wrapped String in AppError
            Resource.Error(AppError.Network.Unknown(e.message ?: "Update failed"))
        }
    }

    override suspend fun uploadAvatar(avatar: ByteArray): Resource<String> = withContext(dispatchers.io) {
        try {
            val avatarUrl = apiService.uploadAvatar(avatar)
            Resource.Success(avatarUrl)
        } catch (e: Exception) {
            // FIXED: Wrapped String in AppError
            Resource.Error(AppError.Network.Unknown(e.message ?: "Upload failed"))
        }
    }

    override suspend fun updateDisplayName(name: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBio(bio: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBiometricEnabled(enabled: Boolean): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePaymentPinEnabled(enabled: Boolean): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePushNotificationsEnabled(enabled: Boolean): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}