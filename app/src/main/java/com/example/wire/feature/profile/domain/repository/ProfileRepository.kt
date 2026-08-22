package com.example.wire.feature.profile.domain.repository

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun observeProfile(): Flow<Profile?>
    suspend fun getProfile(): Resource<Profile>
    suspend fun updateProfile(profile: Profile): Resource<Profile>
    suspend fun uploadAvatar(avatar: ByteArray): Resource<String>
    suspend fun updateDisplayName(name: String): Resource<String>
    suspend fun updateBio(bio:String): Resource<Unit>
    suspend fun updateBiometricEnabled(enabled: Boolean): Resource<Unit>
    suspend fun updatePaymentPinEnabled(enabled:Boolean):Resource<Unit>
    suspend fun updatePushNotificationsEnabled(enabled:Boolean):Resource<Unit>
    suspend fun deleteAccount(): Resource<Unit>
    suspend fun signOut()

}