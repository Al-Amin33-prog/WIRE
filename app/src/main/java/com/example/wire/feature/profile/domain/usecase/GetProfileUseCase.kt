package com.example.wire.feature.profile.domain.usecase

import com.example.wire.core.domain.base.FlowUseCase
import com.example.wire.feature.profile.domain.model.Profile
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject





class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : FlowUseCase<Unit, Profile?>() {

    override  fun invoke(params: Unit): Flow<Profile?> {
        return profileRepository.observeProfile()
    }
}
