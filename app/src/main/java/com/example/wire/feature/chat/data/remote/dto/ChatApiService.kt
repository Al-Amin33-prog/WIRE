package com.example.wire.feature.chat.data.remote.dto

import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {
    @GET("chat/history/{chatId}")
    suspend fun getChatHistory(
        @Path("chatId") chatId: String
    ): List<MessageDto> //  map these to your domain Message

    @POST("chat/send")
    suspend fun sendMessage(
        @Body request: ChatActionDto
    ): Response<Unit>

    @GET("chat/recent")
    suspend fun getRecentChats(): List<ChatDto>

}