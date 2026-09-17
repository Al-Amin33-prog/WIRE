package com.example.wire.feature.profile.data.remote


import com.example.wire.feature.profile.data.remote.dto.ProfileDto
import com.example.wire.feature.profile.data.remote.dto.UpdateProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody

interface ProfileApiService {

    @GET("api/profile/me")
    suspend fun getProfile(): Response<ProfileDto>

    @PATCH("api/profile/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<ProfileDto>

    @Multipart
    @POST("api/profile/avatar")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part
    ): Response<Map<String, String>>

    @DELETE("api/profile/me")
    suspend fun deleteAccount(): Response<Unit>
}


