package com.example.wire.usecases

import app.cash.turbine.test
import com.example.wire.core.common.util.Resource
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import com.example.wire.feature.notifications.domain.usecase.*
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationUseCasesTest {

    private lateinit var useCases: NotificationUseCases
    private val repository = mockk<NotificationRepository>(relaxed = true)

    @Before
    fun setup() {
        useCases = NotificationUseCases(
            getNotifications = GetNotificationsUseCase(repository),
            markAsRead = MarkAsReadUseCase(repository),
            clearAll = ClearAllUseCase(repository)
        )
    }

    @Test
    fun `getNotifications should return Success resource with list`() = runTest {
        val dummyList = listOf(
            WireNotification("1", "Money", "Got $50", NotificationType.PAYMENT_RECEIVED, 123L, false)
        )
        every { repository.getNotifications() } returns flowOf(dummyList)

        useCases.getNotifications().test {
            val result = awaitItem()
            assertTrue(result is Resource.Success)
            assertEquals(1, (result as Resource.Success).data.size)
            assertEquals("Money", result.data[0].title)
            awaitComplete()
        }
    }

    @Test
    fun `markAsRead should return Success resource and call repo`() = runTest {
        val testId = "notif_123"
        val result = useCases.markAsRead(testId)

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.markAsRead(testId) }
    }

    @Test
    fun `clearAll should return Success resource and call repo`() = runTest {
        val result = useCases.clearAll()

        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { repository.clearAll() }
    }
}