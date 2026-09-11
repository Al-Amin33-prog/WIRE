package com.example.wire.core.network.websocket

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.example.wire.core.di.ApplicationScope
import com.example.wire.core.domain.dispatcher.CoroutineDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
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
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("WireWebSocket", message)
                }
            }
        }
    }
    private var session: DefaultClientWebSocketSession? = null

    private var reconnectionJob: Job? = null
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        observeSystemNetwork()
    }

    override fun connectionState() = _state.asStateFlow()
    override fun observeMessages(): Flow<String> = incomingMessages

    override suspend fun connect() {
        // If already connecting/connected, don't start another attempt
        if (_state.value is WebSocketState.Connected || _state.value is WebSocketState.Connecting) return
        
        // Cancel any existing retry loops to start fresh
        reconnectionJob?.cancel()
        internalConnect()
    }

    private suspend fun internalConnect() {
        try {
            _state.value = WebSocketState.Connecting
            Log.d("WireWebSocket", "Attempting connection to ${WebSocketConfig.BASE_URL}")
            session = client.webSocketSession { url(WebSocketConfig.BASE_URL) }
            _state.value = WebSocketState.Connected
            Log.d("WireWebSocket", "Connected successfully")
            reconnectionJob?.cancel()
            listenForMessages()
        } catch (e: Exception) {
            Log.e("WireWebSocket", "Connection failed: ${e.message}")
            handleDisconnection()
        }
    }

    private fun handleDisconnection() {
        if (reconnectionJob?.isActive == true) return

        reconnectionJob = applicationScope.launch(dispatchers.io) {
            var retryDelay = 2000L // Start with 2 seconds
            while (isActive) {
                _state.value = WebSocketState.Connecting
                try {
                    Log.d("WireWebSocket", "Retrying connection...")
                    session = client.webSocketSession { url(WebSocketConfig.BASE_URL) }
                    _state.value = WebSocketState.Connected
                    listenForMessages()
                    return@launch 
                } catch (e: Exception) {
                    Log.w("WireWebSocket", "Retry failed, waiting ${retryDelay}ms")
                    delay(retryDelay)
                    // Exponential backoff up to 30 seconds
                    retryDelay = (retryDelay * 2).coerceAtMost(30_000L)
                }
            }
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
        if (session == null || _state.value !is WebSocketState.Connected) {
            Log.d("WireWebSocket", "SendMessage: Not connected, triggering recovery")
            connect()
            throw Exception("Not connected to server")
        }
        try {
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            Log.e("WireWebSocket", "Send failed: ${e.message}")
            handleDisconnection()
            throw e
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