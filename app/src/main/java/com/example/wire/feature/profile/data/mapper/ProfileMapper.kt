package com.example.wire.feature.profile.data.mapper

import com.example.wire.core.database.entity.UserEntity
import com.example.wire.feature.profile.data.remote.dto.ProfileDto
import com.example.wire.feature.profile.data.remote.dto.ProfileUpdateDto
import com.example.wire.feature.profile.domain.model.Profile

// 1. Remote -> Domain (Logic Layer)
fun ProfileDto.toDomain() = Profile(
    id = id,

    username = username,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    avatarUrl = avatarUrl,
    isVerified = isVerified,
    joinedAt = joinedAt,
    displayName = TODO(),
    wireId = TODO(),
    totalSent = TODO(),
    totalTransfers = TODO(),
    contactCount = TODO(),
    isBiometricEnabled = TODO(),
    isPaymentPinEnabled = TODO(),
    isPushNotificationEnabled = TODO(),
    currency = TODO(),
    appVersion = TODO()
)

// 2. Domain -> Remote (Saving to Cloud)
fun Profile.toDto() = ProfileDto(
    id = id,

    username = username,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    avatarUrl = avatarUrl,
    isVerified = isVerified,
    joinedAt = joinedAt,
    fullName = TODO()
)

// 3. Remote -> Entity (Anchoring in Room)
fun ProfileDto.toEntity() = UserEntity(
    id = id,
    fullName = fullName,
    username = username,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    avatarUrl = avatarUrl,
    isVerified = isVerified,
    joinedAt = joinedAt
)

// 4. Entity -> Domain (Loading from Room for the UI)
fun UserEntity.toDomain() = Profile(
    id = id,
    //fullName = fullName,
    username = username,
    email = email,
    phoneNumber = phoneNumber,
    bio = bio,
    avatarUrl = avatarUrl,
    isVerified = isVerified,
    joinedAt = joinedAt,
    displayName = TODO(),
    wireId = TODO(),
    totalSent = TODO(),
    totalTransfers = TODO(),
    contactCount = TODO(),
    isBiometricEnabled = TODO(),
    isPaymentPinEnabled = TODO(),
    isPushNotificationEnabled = TODO(),
    currency = TODO(),
    appVersion = TODO()
)

fun Profile.toUpdateDto() = ProfileUpdateDto(

    username = username,
    bio = bio,
    phoneNumber = phoneNumber,
    email = email,
    fullName = TODO()
    // Notice we EXCLUDE 'id' and 'joinedAt' as the API usually doesn't allow changing them
)