package com.example.wire.feature.auth.data.remote.dto

data class AuthUserDto(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isEmailVerified: Boolean
)