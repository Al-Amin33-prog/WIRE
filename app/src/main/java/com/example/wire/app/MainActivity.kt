package com.example.wire.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity

import com.example.wire.core.ui.util.LocalFragmentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() { // Changed to FragmentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // This "provides" the activity to all screens in your app
            CompositionLocalProvider(LocalFragmentActivity provides this) {

                // Your AppNavHost or Root Screen here

            }
        }
    }
}
