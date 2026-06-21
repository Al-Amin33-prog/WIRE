package com.example.wire.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean,
    val phone: String? = null
)
