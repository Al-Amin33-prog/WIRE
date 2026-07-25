package com.example.wire.core.feature.security.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SecurityPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    companion object{
        private val PENDING_SYNC = booleanPreferencesKey("pending_sync")
        private val USER_PIN_HASH = stringPreferencesKey("user_pin_hash")
        private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }
    val pinHash: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_PIN_HASH]

    }
    val biometricEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED]?: false

    }
    val pendingSync:  Flow<Boolean> = dataStore.data.map {
        it[PENDING_SYNC] ?: false
    }
    suspend fun setPendingSync(
        pending: Boolean
   ){
        dataStore.edit {
            it[PENDING_SYNC] = pending
        }
    }


    suspend fun savePinHash(hash:String){
        dataStore.edit { preferences ->
            preferences[USER_PIN_HASH] = hash

        }
    }
    suspend fun setBiometricEnabled(enabled:Boolean){
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }
    suspend fun clearSecurity(){
        dataStore.edit {
            it.remove(USER_PIN_HASH)
            it.remove(BIOMETRIC_ENABLED)
        }
    }


}



