package com.example.wire.feature.payments.data.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wire.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentActionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: PaymentRepository // Inject via Hilt

    override fun onReceive(context: Context, intent: Intent) {
        val requestId = intent.getStringExtra("REQUEST_ID")
        if (intent.action == "ACTION_DECLINE_PAYMENT") {
            // SYMPHONY: Background thread call to Ktor to cancel
            CoroutineScope(Dispatchers.IO).launch {
                repository.confirmPayment(requestId ?: "", "DECLINED")
                // Remove notification
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(requestId.hashCode())
            }
        }
    }
}