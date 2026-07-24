package com.example.wire.core.data.repository



import com.example.wire.core.common.util.PerformanceMonitor
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import com.example.wire.core.domain.usecase.SyncChatsUseCase
import com.example.wire.core.domain.usecase.SyncContactUseCase
import com.example.wire.core.domain.usecase.SyncNotificationUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface SyncRepository {
    suspend fun syncAll()
}

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val syncNotifications: SyncNotificationUseCase,
    private val syncContacts: SyncContactUseCase,
    private val syncChats: SyncChatsUseCase,
    private val performanceMonitor: PerformanceMonitor,
    private val dispatcher: CoroutineDispatchers,
) : SyncRepository {

    override suspend fun syncAll() = withContext(dispatcher.io) {
        val startTime = System.currentTimeMillis()
        try {
            syncNotifications()
            syncContacts()
            syncChats()


            // Record Performance
            val duration = System.currentTimeMillis() - startTime
            performanceMonitor.recordEvent(duration)
            println("WIRE_HEALTH: P99 Sync Latency is ${performanceMonitor.getP99()} ms")

        } catch (e: Exception) {
            performanceMonitor.recordEvent(5000)
            e.printStackTrace()
        }
    }
}