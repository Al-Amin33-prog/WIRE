package com.example.wire.feature.notifications.data.local

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.wire.R
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.feature.chat.data.receiver.ChatNotificationReceiver
import com.example.wire.feature.notifications.domain.model.NotificationType
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    private val context: Context
) : NotificationHandler {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showSystemAlert(title: String, message: String, type: NotificationType) {
        val channelId = "wire_alerts"
        // ... (Your existing intent and pendingIntent logic)

        createChannel(channelId, "Wire Alerts")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_important_24px)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // MOVE THIS OUTSIDE showSystemAlert
    override fun showChatNotification(chatId: String, senderName: String, message: String) {
        val channelId = "chat_channel"
        createChannel(channelId, "Messages")

        val remoteInput = RemoteInput.Builder("KEY_TEXT_REPLY")
            .setLabel("Reply")
            .build()

        val replyIntent = Intent(context, ChatNotificationReceiver::class.java).apply {
            action = "ACTION_REPLY"
            putExtra("CHAT_ID", chatId)
        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            chatId.hashCode(),
            replyIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send, "Reply", replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_important_24px) // Use your app icon
            .setContentTitle(senderName)
            .setContentText(message)
            .addAction(replyAction)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(chatId.hashCode(), builder.build())
    }

    private fun createChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }
}