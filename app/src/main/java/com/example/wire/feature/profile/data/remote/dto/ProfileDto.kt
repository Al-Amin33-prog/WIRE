package com.example.wire.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class ProfileDto(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val bio: String?,
    val avatarUrl: String?,
    val isVerified: Boolean,
    val joinedAt: Long,
)

@Serializable
data class ProfileUpdateDto(
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val bio: String?,
)