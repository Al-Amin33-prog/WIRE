package com.example.wire.core.database.dao



import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.wire.core.database.entity.UserEntity

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUserProfile(user: UserEntity)

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
}