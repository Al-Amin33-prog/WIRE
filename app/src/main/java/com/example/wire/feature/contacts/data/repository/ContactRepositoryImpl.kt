package com.example.wire.feature.contacts.data.repository

import android.content.Context
import android.provider.ContactsContract
import com.example.wire.core.database.dao.ChatDao
import com.example.wire.core.database.entity.ChatEntity
import com.example.wire.feature.contacts.domain.model.ContactUser
import com.example.wire.feature.contacts.data.repository.remote.ContactApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val api: ContactApiService,

    @field:ApplicationContext private val context: Context
) : ContactRepository {

    override fun getSyncedContacts(): Flow<List<ContactUser>> {

        return chatDao.getSyncedContacts().map { entities ->
            entities.map { entity ->
                ContactUser(
                    id = entity.chatId,
                    name = entity.contactName,
                    lastMessage = entity.lastMessage,
                    avatarColor = entity.avatarColor
                )
            }
        }
    }

    override suspend fun syncContacts() {
        withContext(Dispatchers.IO) {
            try {

                val phoneNumbers = getLocalPhoneContacts()

                if (phoneNumbers.isNotEmpty()) {
                    // 2. Send to Backend to find matches
                    val registeredUsers = api.checkRegisteredUsers(phoneNumbers)


                    registeredUsers.forEach { userDto ->
                        chatDao.upsertChat(
                            ChatEntity(
                                chatId = userDto.uid,
                                contactName = userDto.displayName ?: "Wire User",
                                lastMessage = "Recently joined Wire",
                                timestamp = System.currentTimeMillis(),
                                avatarColor = -12345,
                                isContact = true // This marks them for the Contact list
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Helper function to read the Android Phone Book
    private fun getLocalPhoneContacts(): List<String> {
        val contactList = mutableListOf<String>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val number = it.getString(numberIndex).replace("\\s".toRegex(), "")
                contactList.add(number)
            }
        }
        return contactList
    }
}