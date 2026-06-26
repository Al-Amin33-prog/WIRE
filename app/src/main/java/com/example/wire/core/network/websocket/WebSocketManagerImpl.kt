package com.example.wire.core.network.websocket

import com.example.wire.core.database.dao.MessageDao
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.feature.chat.data.mapper.toEntity

import com.example.wire.feature.chat.data.remote.dto.ChatActionDto
import com.example.wire.feature.notifications.domain.model.NotificationType
import com.example.wire.feature.notifications.domain.model.WireNotification
import com.example.wire.feature.notifications.domain.repository.NotificationRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val notificationHandler: NotificationHandler,
    private val messageDao: MessageDao
) : WebSocketManager {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null
    private val _isTyping = MutableStateFlow(false)
    override fun isTyping() = _isTyping.asStateFlow()

    private val incomingMessages = MutableSharedFlow<String>()

    override fun connectionState() = _state.asStateFlow()

    override suspend fun connect() {
        try {
            session = client.webSocketSession {
                url(WebSocketConfig.BASE_URL)
            }
            _state.value = WebSocketState.Connected
            listenForMessages()
        } catch (e: Exception) {
            _state.value = WebSocketState.Disconnected
        }
    }

    private fun listenForMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session?.incoming?.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val jsonString = frame.readText()
                        try {
                            val chatAction = Json.decodeFromString<ChatActionDto>(jsonString)

                            when(chatAction.action) {
                                "PAYMENT" -> {
                                    val msg = chatAction.message
                                    val paymentNotif = WireNotification(
                                        // FIXED: Use java.util.UUID
                                        id = msg?.id ?: UUID.randomUUID().toString(),
                                        title = "Payment Received",
                                        content = "You received $${msg?.content} from ${msg?.senderId}",
                                        type = NotificationType.PAYMENT_RECEIVED,
                                        timestamp = System.currentTimeMillis(),
                                        isRead = false
                                    )
                                    notificationRepository.saveNotification(paymentNotif)

                                    notificationHandler.showSystemAlert(
                                        title = "Money Received! 💰",
                                        message = paymentNotif.content,
                                        type = NotificationType.PAYMENT_RECEIVED
                                    )

                                    incomingMessages.emit(jsonString)
                                }

                                "SEND" -> {
                                    val msg = chatAction.message
                                   if (msg != null){
                                       CoroutineScope(Dispatchers.IO).launch {
                                           messageDao.insertMessage(msg.toEntity(chatId = "get_chat_id_from_logic"))
                                       }
                                   }
                                    notificationHandler.showSystemAlert(
                                        title = chatAction.message?.senderId ?: "New Message",
                                        message = chatAction.message?.content ?: "",
                                        type = NotificationType.MESSAGE
                                    )
                                    incomingMessages.emit(jsonString)
                                }

                                "TYPING_ON" -> _isTyping.value = true
                                "TYPING_OFF" -> _isTyping.value = false
                                "DELETE" -> incomingMessages.emit(jsonString)
                            }
                        } catch (e: Exception) {
                            incomingMessages.emit(jsonString)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = WebSocketState.Disconnected
            }
        }
    }

    override suspend fun disconnect() {
        session?.close()
        _state.value = WebSocketState.Disconnected
    }

    override suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }

    override fun observeMessages(): Flow<String> {
        return incomingMessages
    }
}