package com.example.wire.core.common.util



import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class PerformanceMonitor @Inject constructor() {

    // We only store the last 50 events to keep RAM impact tiny
    private val latencyBuffer = mutableListOf<Long>()
    private val MAX_BUFFER_SIZE = 50

    fun recordEvent(durationMs: Long) {
        synchronized(latencyBuffer) {
            if (latencyBuffer.size >= MAX_BUFFER_SIZE) {
                latencyBuffer.removeAt(0) // Remove oldest (Circular Buffer)
            }
            latencyBuffer.add(durationMs)
        }
    }

    fun getP99(): Int {
        return synchronized(latencyBuffer) {
            if (latencyBuffer.isEmpty()) return 0
            val sorted = latencyBuffer.sorted()
            val index = (sorted.size * 0.99).roundToInt().coerceAtMost(sorted.size - 1)
            sorted[index].toInt()
        }
    }

    // Logic to check if the system is "Heavy"
    fun isSystemOverloaded(): Boolean = getP99() > 800 // If p99 > 800ms, it's slow
}