package com.example.wire.feature.profile.domain.usecase

import com.example.wire.core.domain.base.BaseUseCase
import com.example.wire.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateSecuritySettingsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseUseCase<UpdateSecuritySettingsUseCase.Params, Result<Unit>>() {

    data class Params(
        val biometricEnabled: Boolean? = null,
        val paymentPinEnabled: Boolean? = null,
        val pushNotificationsEnabled: Boolean? = null
    )

    override suspend fun invoke(params: Params): Result<Unit> {
        return try {
            params.biometricEnabled?.let {
                profileRepository.updateBiometricEnabled(it)
            }
            params.paymentPinEnabled?.let {
                profileRepository.updatePaymentPinEnabled(it)
            }
            params.pushNotificationsEnabled?.let {
                profileRepository.updatePushNotificationsEnabled(it)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}