package com.example.wire.core.network.websocket

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.wire.core.di.ApplicationScope
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
) : WebSocketManager {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
    private val incomingMessages = MutableSharedFlow<String>()
    private val client = HttpClient(CIO) { install(WebSockets) }
    private var session: DefaultClientWebSocketSession? = null

    private var reconnectionJob: Job? = null
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        observeSystemNetwork()
    }

    override fun connectionState() = _state.asStateFlow()
    override fun observeMessages(): Flow<String> = incomingMessages

    override suspend fun connect() {
        // Don't restart if already connected or currently trying to connect
        if (_state.value is WebSocketState.Connected || _state.value is WebSocketState.Connecting) return
        internalConnect()
    }

    private suspend fun internalConnect() {
        try {
            _state.value = WebSocketState.Connecting // UI can now show "Connecting..."
            session = client.webSocketSession { url(WebSocketConfig.BASE_URL) }
            _state.value = WebSocketState.Connected
            reconnectionJob?.cancel() // Success: kill any active retry loops
            listenForMessages()
        } catch (e: Exception) {
            handleDisconnection()
        }
    }

    private fun handleDisconnection() {
        // Prevent multiple reconnection loops from running at once
        if (reconnectionJob?.isActive == true) return

        reconnectionJob = applicationScope.launch(dispatchers.io) {
            val startTime = System.currentTimeMillis()
            val maxRetryDuration = 30_000L // 30 seconds

            while (isActive && (System.currentTimeMillis() - startTime < maxRetryDuration)) {
                _state.value = WebSocketState.Connecting // Keep UI in "Connecting" mode

                try {
                    session = client.webSocketSession { url(WebSocketConfig.BASE_URL) }
                    _state.value = WebSocketState.Connected
                    listenForMessages()
                    return@launch // Exit loop on success
                } catch (e: Exception) {
                    // Plan: Reconnect every 1 second
                    delay(1000)
                }
            }

            // 30 seconds passed with no internet: Fully go off
            _state.value = WebSocketState.Disconnected
        }
    }

    private fun listenForMessages() {
        applicationScope.launch(dispatchers.io) {
            try {
                session?.incoming?.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        incomingMessages.emit(frame.readText())
                    }
                }
            } catch (e: Exception) {
                // If the stream breaks (Network drop), trigger the 30s logic
                handleDisconnection()
            }
        }
    }

    private fun observeSystemNetwork() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // OS says Internet is back! Recovery trigger.
                applicationScope.launch(dispatchers.io) {
                    connect()
                }
            }
        })
    }

    override suspend fun sendMessage(message: String) {
        try {
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            handleDisconnection()
        }
    }

    override suspend fun disconnect() {
        reconnectionJob?.cancel()
        session?.close()
        _state.value = WebSocketState.Disconnected



        // 2. Close the actual Ktor session
        try {
            session?.close(CloseReason(CloseReason.Codes.NORMAL, "User Logged Out"))
        } catch (e: Exception) {
            // Already closed or network lost
        } finally {
            session = null // CRITICAL: Nulling this allows Profiler to see memory release
        }

        // 3. Update state so UI stops observing
        _state.value = WebSocketState.Disconnected
    }

    private val _isTyping = MutableStateFlow(false)
    override fun isTyping() = _isTyping.asStateFlow()
}