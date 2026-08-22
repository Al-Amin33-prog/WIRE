package com.example.wire.feature.profile.domain.usecase

import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor (
    private val repository: ProfileRepository
){
    suspend operator fun invoke(profile: Profile) = repository.updateProfile(profile)
}