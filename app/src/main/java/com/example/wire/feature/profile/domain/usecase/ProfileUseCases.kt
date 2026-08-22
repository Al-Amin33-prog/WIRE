package com.example.wire.feature.profile.domain.usecase

import javax.inject.Inject

data class ProfileUseCases @Inject constructor(
    val getProfile: GetProfileUseCase,
    val updateProfile: UpdateProfileUseCase,
    val uploadAvatar: UploadAvatarUseCase
)
