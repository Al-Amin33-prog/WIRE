package com.example.wire.core.database.dao


import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.wire.core.database.entity.ChatEntity

@Dao
interface ChatDao {
    // This is what your Search Bar will call!
    @Query("SELECT * FROM chats WHERE contactName LIKE '%' || :query || '%'")
    fun searchChats(query: String): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<ChatEntity>>
}