package com.example.wire.feature.profile.domain.usecase



import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseUseCase<UpdateProfileUseCase.Params, Result<Unit>>() {

    data class Params(
        val displayName: String? = null,
        val bio: String? = null
    )

    override suspend fun invoke(params: Params): Result<Unit> {
        return try {
            params.displayName?.let {
                if (it.isBlank()) return Result.failure(
                    IllegalArgumentException("Display name cannot be empty")
                )
                profileRepository.updateDisplayName(it)
            }
            params.bio?.let { profileRepository.updateBio(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
