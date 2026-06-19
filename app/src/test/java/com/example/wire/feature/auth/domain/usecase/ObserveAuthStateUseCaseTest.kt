package com.example.wire.feature.auth.domain.usecase

import com.example.wire.feature.auth.domain.model.AuthUser
import com.example.wire.feature.auth.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ObserveAuthStateUseCaseTest {

    private val repository = mockk<AuthRepository>()
    private val useCase = ObserveAuthStateUseCase(repository)

    @Test
    fun `invoke returns flow of user from repository`() = runBlocking {
        val user = AuthUser(uid = "123", email = "test@example.com", displayName = "Test", token = "token")
        every { repository.observeAuthState() } returns flowOf(user)

        val result = useCase(Unit).first()

        assertEquals(user, result)
    }

    @Test
    fun `invoke returns flow of null when user is not authenticated`() = runBlocking {
        every { repository.observeAuthState() } returns flowOf(null)

        val result = useCase(Unit).first()

        assertNull(result)
    }
}
