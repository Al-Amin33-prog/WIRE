package com.example.wire.feature.chat.data.receiver

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wire.core.di.ApplicationScope // Your Qualifier
import com.example.wire.feature.chat.domain.repository.ChatRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var chatRepository: ChatRepository

    // FIX: Inject your global application scope here
    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val chatId = intent.getStringExtra("CHAT_ID") ?: return

        if (action == "ACTION_REPLY") {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            val replyText = remoteInput?.getCharSequence("KEY_TEXT_REPLY")?.toString()

            if (!replyText.isNullOrBlank()) {
                // Use the injected applicationScope
                // This keeps the task alive even after onReceive returns
                applicationScope.launch {
                    chatRepository.sendMessage(chatId, replyText)
                }
            }
        }
    }
}