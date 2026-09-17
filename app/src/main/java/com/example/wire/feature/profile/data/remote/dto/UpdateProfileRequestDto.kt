package com.example.wire.feature.profile.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class UpdateProfileRequestDto(
    val displayName: String? = null,
    val bio: String? = null
)
