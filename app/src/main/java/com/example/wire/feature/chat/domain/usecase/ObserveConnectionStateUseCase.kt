package com.example.wire.feature.chat.domain.usecase



import com.example.wire.core.network.websocket.WebSocketState
import com.example.wire.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionStateUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<WebSocketState> {
        return repository.observeConnectionState()
    }
}