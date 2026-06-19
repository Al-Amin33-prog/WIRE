package com.example.wire.feature.auth.data.mapper

import com.example.wire.feature.auth.data.remote.dto.AuthUserDto
import com.example.wire.feature.auth.domain.model.AuthUser

fun AuthUserDto.toDomain(): AuthUser {
    return AuthUser(
        uid = uid,
        email = email ?: "",
        displayName = displayName,
        isEmailVerified = isEmailVerified,
        token = ""
    )
}