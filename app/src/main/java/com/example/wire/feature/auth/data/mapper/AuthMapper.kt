package com.example.wire.feature.auth.data.mapper

import com.example.wire.feature.auth.data.remote.dto.AuthUserDto
import com.example.wire.feature.auth.domain.model.AuthUser

fun AuthUserDto.toDomain(): AuthUser {
    val derivedUsername = username ?: generateUsername(displayName, email, uid)
    return AuthUser(
        uid = uid,
        email = email ?: "",
        displayName = displayName,
        isEmailVerified = isEmailVerified,
        token = "",
        phone = phone ?: "",
        username = derivedUsername
    )
}

fun generateUsername(displayName: String?, email: String?, uid: String): String {
    val base = displayName?.takeIf { it.isNotBlank() }
        ?: email?.substringBefore("@")
        ?: "user"
    val cleanBase = base.lowercase().replace(Regex("[^a-z0-9_]"), "_").take(15)
    val suffix = uid.take(4).lowercase()
    return "${cleanBase}_$suffix"
}
