package com.example.wire.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.wire.app.navigation.AppNavHost
import com.example.wire.app.navigation.NavigatorImpl
import com.example.wire.core.ui.theme.WireTheme
import com.example.wire.core.ui.util.LocalFragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    @Inject
    lateinit var navigatorImpl: NavigatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Note: WebSocket Listener is already started in WireApp.kt

        enableEdgeToEdge()
        setContent {
            // 1. Initialize the NavController for this Activity session
            val navController = rememberNavController()
            
            // 2. Link it to your NavigatorImpl so the app can navigate via UseCases
            navigatorImpl.navController = navController

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

                    // 3. AppNavHost will now correctly find the navController
                    AppNavHost(navigatorImpl = navigatorImpl)
                }
            }
        }
    }
}
