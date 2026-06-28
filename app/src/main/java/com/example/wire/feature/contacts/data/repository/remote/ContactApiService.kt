package com.example.wire.feature.contacts.data.repository.remote



import com.example.wire.feature.auth.data.remote.dto.AuthUserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactApiService {
    @POST("users/sync-contacts")
    suspend fun checkRegisteredUsers(@Body numbers: List<String>): List<AuthUserDto>
}