package com.example.wire.interceptor

import com.example.wire.core.network.interceptors.AuthInterceptor
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockTask = mockk<Task<GetTokenResult>>()
    private val mockResult = mockk<GetTokenResult>()

    @Before
    fun setup() {
        mockWebServer = MockWebServer() // Fixed typo here
        mockWebServer.start()

        // Mock the Coroutine await() extension for Firebase Tasks
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.getIdToken(false) } returns mockTask

        // This is the magic for your "Blind Build" - mocking the network token
        coEvery { mockTask.await() } returns mockResult
        every { mockResult.token } returns "fake-firebase-jwt-123"

        val interceptor = AuthInterceptor(mockAuth)
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `interceptor should add Firebase Token to Authorization header`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()

        okHttpClient.newCall(request).execute()

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("Bearer fake-firebase-jwt-123", recordedRequest.getHeader("Authorization"))
    }
}