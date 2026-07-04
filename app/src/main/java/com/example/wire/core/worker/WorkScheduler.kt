package com.example.wire.core.worker

import android.content.Context
import androidx.work.* // This brings in the correct Constraints and WorkManager
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
    @ApplicationContext private val context: Context
) : WorkScheduler {

    // Removed the weird '.build' and '.getInstance' syntax errors
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleSync() {
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
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<MessageRetryWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "message_retry",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}