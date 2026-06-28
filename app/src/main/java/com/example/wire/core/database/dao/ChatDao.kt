package com.example.wire.core.database.dao


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import com.example.wire.core.database.entity.ChatEntity

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats WHERE contactName LIKE '%' || :query || '%'")
    fun searchChats(query: String): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<ChatEntity>>


    @Query("UPDATE chats SET lastMessage = :lastMessage, timestamp = :timestamp WHERE chatId = :chatId")
    suspend fun updateChatPreview(chatId: String, lastMessage: String, timestamp: Long)
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)



    @Query("SELECT * FROM chats WHERE isContact = 1 ORDER BY contactName ASC")
    fun getSyncedContacts(): Flow<List<ChatEntity>>
}