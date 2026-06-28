package com.example.wire.usecases


import app.cash.turbine.test
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import com.example.wire.feature.notifications.domain.usecase.ClearAllUseCases
import com.example.wire.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.example.wire.feature.notifications.domain.usecase.MarkAsReadUseCase
import com.example.wire.feature.notifications.domain.usecase.NotificationUseCases
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
            clearAll = ClearAllUseCases(repository)
        )
    }

    @Test
    fun `getNotifications should return stream of notifications from repository`() = runTest {
        // 1. Arrange: Create dummy notifications
        val dummyList = listOf(
            WireNotification(
                id = "1",
                title = "Money Received",
                content = "You got $50",
                type = NotificationType.PAYMENT_RECEIVED,
                timestamp = 123456789L,
                isRead = false
            )
        )
        every { repository.getNotifications() } returns flowOf(dummyList)

        // 2. Act & Assert: Use Turbine to observe the Flow
        useCases.getNotifications().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Money Received", result[0].title)
            awaitComplete()
        }
    }

    @Test
    fun `markAsRead should call repository with correct id`() = runTest {
        // 1. Act
        val testId = "notif_123"
        useCases.markAsRead(testId)

        // 2. Assert
        coVerify(exactly = 1) { repository.markAsRead(testId) }
    }

    @Test
    fun `clearAll should trigger clearAll in repository`() = runTest {
        // 1. Act
        useCases.clearAll()

        // 2. Assert
        coVerify(exactly = 1) { repository.clearAll() }
    }
}