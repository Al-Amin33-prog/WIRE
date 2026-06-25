package com.example.wire.feature.notifications.data.local



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.wire.R
import com.example.wire.core.network.notification.NotificationHandler
import com.example.wire.feature.notifications.domain.model.NotificationType
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    private val context: Context
) : NotificationHandler {

    override fun showSystemAlert(title: String, message: String, type: NotificationType) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "wire_alerts"
        val intent = Intent(context, Class.forName("com.example.wire.MainActivity")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Pass the destination as an extra
            putExtra("NAVIGATION_TARGET", when(type) {
                NotificationType.PAYMENT_RECEIVED, NotificationType.PAYMENT_REQUEST -> "notifications"
                NotificationType.MESSAGE -> "chat_list"
                else -> "notifications"
            })
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Wire Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_important_24px) // Ensure this icon exists in res/drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}