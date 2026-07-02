package com.example.wire.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.example.wire.app.navigation.AppNavHost
import com.example.wire.app.navigation.NavigatorImpl
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.core.ui.util.LocalFragmentActivity
import com.example.wire.feature.chat.domain.usecase.ObserveWebSocketEventsUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    @Inject
    lateinit var navigatorImpl: NavigatorImpl
    
    @Inject
    lateinit var observeWebSocketEventsUseCase: ObserveWebSocketEventsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Start listening for WebSocket events globally
        observeWebSocketEventsUseCase()

        enableEdgeToEdge()
        setContent {
            WireTheme(darkTheme = false) {
                CompositionLocalProvider(LocalFragmentActivity provides this) {

                    LaunchedEffect(intent) {
                        intent?.let { navigatorImpl.handleDeepLink(it) }
                    }

                    DisposableEffect(Unit) {
                        val listener = Consumer<Intent> { intent ->
                            navigatorImpl.handleDeepLink(intent)
                        }
                        addOnNewIntentListener(listener)
                        onDispose { removeOnNewIntentListener(listener) }
                    }

                    AppNavHost(navigatorImpl = navigatorImpl)
                }
            }
        }
    }
}
