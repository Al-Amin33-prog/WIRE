package com.example.wire.core.feature.security.data.local

import com.example.wire.core.feature.security.data.datastore.SecurityPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SecurityLocalDataSourceImpl @Inject constructor(
    private val securityPreferenceDataStore: SecurityPreferences
): SecurityLocalDataSource{
    override suspend fun savePinHash(pinHash:String) {
        securityPreferenceDataStore.savePinHash(pinHash)
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

    override suspend fun setPendingSync(pending: Boolean) {
        securityPreferenceDataStore.setPendingSync(pending)
    }

    override suspend fun isPendingSync(): Boolean {
        return securityPreferenceDataStore.pendingSync.first()
    }


}