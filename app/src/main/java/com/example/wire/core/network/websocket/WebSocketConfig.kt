package com.example.wire.core.network.websocket





import android.os.Build

object WebSocketConfig {
    private val isEmulator: Boolean
        get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")


    private val hostIp: String
        get() = if (isEmulator) "10.0.2.2" else "192.168.0.168"


    val BASE_URL: String
        get() = "ws://$hostIp:8080/chat"


    const val RECONNECT_INTERVAL = 5000L
}