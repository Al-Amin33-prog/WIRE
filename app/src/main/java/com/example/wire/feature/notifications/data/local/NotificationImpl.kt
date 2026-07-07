package com.example.wire.feature.notifications.data.local

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.wire.R
import com.example.wire.app.MainActivity
import com.example.wire.core.common.constants.NotificationConstants
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.feature.chat.data.receiver.ChatNotificationReceiver
import com.example.wire.feature.notifications.domain.model.NotificationType
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    private val context: Context
) : NotificationHandler {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // We convert the launcher icon to a Bitmap once to save CPU on your 1.2GHz machine
    private val appLogoBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
    }

    override fun showSystemAlert(title: String, message: String, type: NotificationType) {
        createChannel(NotificationConstants.CHANNEL_ALERTS, "Wire Alerts")

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_wire_stat_bar)
            .setLargeIcon(appLogoBitmap)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun showChatNotification(chatId: String, senderName: String, message: String) {
        createChannel(NotificationConstants.CHANNEL_CHAT, "Messages")

        val remoteInput = RemoteInput.Builder("KEY_TEXT_REPLY")
            .setLabel("Reply")
            .build()

        val replyIntent = Intent(context, ChatNotificationReceiver::class.java).apply {
            action = NotificationConstants.ACTION_REPLY
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

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_CHAT)
            .setSmallIcon(R.drawable.ic_wire_stat_bar) // FIXED: Using your App Icon
            .setLargeIcon(appLogoBitmap)
            .setContentTitle(senderName)
            .setContentText(message)
            .addAction(replyAction)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(chatId.hashCode(), builder.build())
    }

    override fun showPaymentRequestNotification(
        requestId: String,
        senderName: String,
        amount: String
    ) {
        createChannel(NotificationConstants.CHANNEL_GENERAL, "General Notifications")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = NotificationConstants.ACTION_PAYMENT_REQUEST
            putExtra("REQUEST_ID", requestId)
            putExtra("AMOUNT", amount)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_wire_stat_bar) // FIXED: Using your App Icon
            .setLargeIcon(appLogoBitmap)
            .setContentTitle("Payment Request 💰")
            .setContentText("$senderName requested $$amount")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(requestId.hashCode(), notification)
    }

    private fun createChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }
}