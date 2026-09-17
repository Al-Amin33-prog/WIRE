package com.example.wire.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable




data class ProfileDto(
    val uid: String,
    val displayName: String,
    val userName: String,
    val email: String,
    val phoneNumber: String?,
    val wireId: String,
    val avatarUrl: String?,
    val bio: String?,
    val isKycVerified: Boolean,
    val totalSent: Double,
    val totalReceived: Double,
    val totalTransfers: Int,
    val contactCount: Int,
    val joinedAt:  Long,
    val appVersion: String
)




