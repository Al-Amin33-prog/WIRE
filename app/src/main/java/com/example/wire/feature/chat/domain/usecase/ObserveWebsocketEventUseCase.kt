package com.example.wire.feature.chat.domain.usecase

import com.example.wire.core.di.ApplicationScope
import com.example.wire.core.network.websocket.WebSocketManager
import com.example.wire.core.network.websocket.WebSocketProcessor
import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ObserveWebSocketEventsUseCase @Inject constructor(
    private val webSocketManager: WebSocketManager,
    // Hilt automatically collects all @IntoSet processors here!
    private val processors: Set<@JvmSuppressWildcards WebSocketProcessor>,
    @ApplicationScope private val scope: CoroutineScope // FIXED: No @field:
) {
    operator fun invoke() {
        scope.launch {
            webSocketManager.observeMessages().collect { jsonString ->
                try {
                    val chatAction = Json.decodeFromString<ChatActionDto>(jsonString)

                    // Find the right feature to handle this event
                    processors
                        .filter{ it.action == chatAction.action }
                        .forEach { processor ->
                            processor.process(chatAction)
                        }

                } catch (e: Exception) {
                    println("Switchboard Error: ${e.message}")
                }
            }
        }
    }
}