package com.example.wire.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.wire.feature.chat.domain.usecase.ObserveWebSocketEventsUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WireApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    // FIX: Must be an override 'val' with a getter for the WorkManager Configuration
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @Inject
    lateinit var observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase

    override fun onCreate() {
        super.onCreate()

        // Starts the global WebSocket switchboard
        observeWebSocketEventsUseCase()
    }
}