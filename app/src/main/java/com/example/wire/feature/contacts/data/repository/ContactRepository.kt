package com.example.wire.feature.contacts.data.repository



import com.example.wire.feature.contacts.domain.model.ContactUser
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    /**
     * Returns a reactive stream of users from the local database
     * who are marked as synced contacts.
     */
    fun getSyncedContacts(): Flow<List<ContactUser>>

    /**
     * Triggers the synchronization process (usually via WorkManager
     * or API) to match phone contacts with the backend.
     */
    suspend fun syncContacts()
}