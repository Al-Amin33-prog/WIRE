package com.example.wire.feature.chat.data.remote.dto

import retrofit2.http.Path
import com.example.wire.feature.chat.domain.model.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {
    @GET("chat/history/{chatId}")
    suspend fun getChatHistory(
        @Path("chatId") chatId: String
    ): List<Message> // You will map these to your domain Message

    @POST("chat/send")
    suspend fun sendMessage(
        @Body request: ChatActionDto
    ): Response<Unit>
}