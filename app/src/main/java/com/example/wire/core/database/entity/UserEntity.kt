package com.example.wire.core.database.entity



import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val bio: String?,
    val avatarUrl: String?,
    val isVerified: Boolean,
    val joinedAt: Long,
    val trustScore: String = "98%"
)