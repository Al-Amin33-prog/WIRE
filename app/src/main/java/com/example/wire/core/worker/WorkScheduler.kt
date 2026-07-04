package com.example.wire.core.worker

import android.content.Context
import android.util.Log
import androidx.work.* // This brings in the correct Constraints and WorkManager
import com.example.wire.core.common.util.PerformanceMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


interface WorkScheduler {
    fun scheduleSync()
    fun scheduleMessageRetry()
}

@Singleton
class WorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val performanceMonitor: PerformanceMonitor
) : WorkScheduler {

    // Removed the weird '.build' and '.getInstance' syntax errors
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleSync() {
        if (performanceMonitor.isSystemOverloaded()) {
            Log.w(
                "WorkScheduler", "System is overloaded, skipping sync")
            return
        }

        // Correct Constraints for WorkManager
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork("global_sync", ExistingWorkPolicy.KEEP, request)
    }

    override fun scheduleMessageRetry() {

        val isOverloaded = performanceMonitor.isSystemOverloaded()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            // FIX: Use setRequiresDeviceIdle instead of setRequiredNetworkRequest.
            // If the CPU is overloaded, we wait until the device is IDLE to retry.
            .setRequiresDeviceIdle(isOverloaded)
            .build()

        val request = OneTimeWorkRequestBuilder<MessageRetryWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            "message_retry",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}