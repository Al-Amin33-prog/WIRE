package com.example.wire.feature.profile.data.repository


import com.example.wire.core.common.util.Resource
import com.example.wire.core.database.dao.UserDao
import com.example.wire.core.datastore.preferences.UserPreferencesDataStore
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.feature.profile.data.remote.ProfileApiService
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService,
    private val userDao: UserDao,
    private val dataStore: UserPreferencesDataStore,
    private val firebaseAuth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers
) : ProfileRepository {
    override fun observeProfile(): Flow<Profile?> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(): Resource<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(profile: Profile): Resource<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadAvatar(avatar: ByteArray): Resource<String> {
        TODO("Not yet implemented")
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

    override suspend fun deleteAccount(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

}

