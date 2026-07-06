package com.example.wire.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wire.core.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    suspend fun updateMessageStatus(messageId: String, status: String)

    @Query("SELECT * FROM messages WHERE status = 'PENDING' OR status = 'FAILED'")
    fun getUnsentMessages(): Flow<List<MessageEntity>>
    @Query("UPDATE messages SET isDeleted = 1 WHERE id = :messageId")
    suspend fun markMessageAsDeleted(messageId: String)

    @Query("UPDATE messages SET isRead = 1 WHERE chatId = :chatId AND senderId != :currentUserId")
    suspend fun markChatAsRead(chatId: String, currentUserId: String)
    @Query("UPDATE messages SET content = :newContent, isEdited = 1, timestamp = :timestamp WHERE id = :messageId")
    suspend fun updateMessageContent(messageId: String, newContent: String, timestamp: Long)







}