package com.example.wire.feature.chat.data.remote.dto

import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {
    @GET("chat/history/{senderId}/{receiverId}")
    suspend fun getChatHistory(
        @Path("senderId") senderId: String,
        @Path("receiverId") receiverId: String,
    ): List<MessageDto> //  map these to your domain Message

    @POST("chat/send")
    suspend fun sendMessage(
        @Body request: ChatActionDto
    ): Response<Unit>

    @GET("chat/recent/{userId}")
    suspend fun getRecentChats(
        @Path("userId") userId:String
    ): List<ChatDto>

}