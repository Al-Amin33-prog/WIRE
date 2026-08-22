package com.example.wire.feature.profile.data.remote

import com.example.wire.feature.profile.data.remote.dto.ProfileDto
import com.example.wire.feature.profile.data.remote.dto.ProfileUpdateDto

interface ProfileApiService {
    suspend fun getProfile(): ProfileDto
    suspend fun updateProfile(profile: ProfileUpdateDto): ProfileDto
    suspend fun uploadAvatar(avatar: ByteArray): String
}
