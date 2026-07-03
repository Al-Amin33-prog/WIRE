package com.example.wire.app

import android.app.Application
import com.example.wire.feature.chat.domain.usecase.ObserveWebSocketEventsUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WireApp : Application() {

    // Hilt provides this automatically because it has an @Inject constructor
    @Inject
    lateinit var observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase

    override fun onCreate() {
        super.onCreate()

        // This starts the global listener exactly ONCE for the app's entire lifetime
        observeWebSocketEventsUseCase()
    }
}