package com.example.wire.core.datastore.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wire_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val IS_BIOMETRIC_ENABLED = booleanPreferencesKey("is_biometric_enabled")
        private val SAVED_USER_EMAIL = stringPreferencesKey("saved_user_email")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    val isBiometricEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_BIOMETRIC_ENABLED] ?: false
        }

    val savedUserEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SAVED_USER_EMAIL] ?: ""
        }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = value
        }
    }

    suspend fun setBiometricEnabled(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_BIOMETRIC_ENABLED] = value
        }
    }

    suspend fun setSavedEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[SAVED_USER_EMAIL] = email
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
