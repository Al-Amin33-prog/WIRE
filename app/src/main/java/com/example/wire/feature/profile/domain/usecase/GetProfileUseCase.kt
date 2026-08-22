package com.example.wire.feature.profile.domain.usecase

import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke() = repository.getProfile()
}