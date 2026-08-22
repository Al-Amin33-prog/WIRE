package com.example.wire.feature.profile.domain.usecase

import com.example.wire.core.common.util.Resource
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(image: ByteArray): Resource<String> {
        return repository.uploadAvatar(image)
    }
}