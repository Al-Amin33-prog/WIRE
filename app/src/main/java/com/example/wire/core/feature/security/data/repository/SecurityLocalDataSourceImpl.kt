package com.example.wire.core.feature.security.data.repository


import com.example.wire.core.feature.security.data.datastore.SecurityPreferencesDataStore
import com.example.wire.core.feature.security.data.local.SecurityLocalDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SecurityLocalDataSourceImpl @Inject constructor(
    private val securityPreferenceDataStore: SecurityPreferencesDataStore
): SecurityLocalDataSource{
    override suspend fun savePinHash(hash: String) {
        securityPreferenceDataStore.savePinHash(hash)
    }

    override suspend fun getPinHash(): String? {
        return securityPreferenceDataStore.pinHash.first()
    }

    override suspend fun hasPin(): Boolean {
        return ! securityPreferenceDataStore.pinHash.first().isNullOrEmpty()
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        securityPreferenceDataStore.setBiometricEnabled(enabled)

    }

    override suspend fun isBiometricEnabled(): Boolean {
        return securityPreferenceDataStore.biometricEnabled.first()

    }

    override suspend fun clearSecurity() {
        securityPreferenceDataStore.clearSecurity()

    }


}