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



@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        private val SAVED_USER_EMAIL = stringPreferencesKey("saved_user_email")

        private val BIOMETRIC_TYPE = stringPreferencesKey("biometric_type")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }



    val savedUserEmail: Flow<String> = dataStore.data
        .map { preferences -> preferences[SAVED_USER_EMAIL] ?: "" }

    val selectedBiometricType: Flow<String> = dataStore.data
        .map{preferences -> preferences[BIOMETRIC_TYPE] ?: "NONE"}

    suspend fun setLoggedIn(value: Boolean) {
    dataStore.edit { preferences -> preferences[IS_LOGGED_IN] = value }
    }



    suspend fun setSavedEmail(email: String) {
        dataStore.edit { preferences -> preferences[SAVED_USER_EMAIL] = email }
    }

    suspend fun clearAll() {
       dataStore.edit { it.clear() }
    }
}
