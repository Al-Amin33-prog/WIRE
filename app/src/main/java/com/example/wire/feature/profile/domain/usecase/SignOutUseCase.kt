package com.example.wire.feature.profile.domain.usecase




import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseUseCase<Unit, Unit>() {

    override suspend fun invoke(params: Unit) {
        profileRepository.signOut()
    }
}