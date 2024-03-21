package com.team.app.service

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat
import com.team.app.R

class NotificationService(
    private val context: Context,
) {
    fun showNotification(messgae : String) {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_fastfood_24)
            .setContentTitle("Food time Notification")
            .setContentText(messgae)

    }
    companion object{
        const val NOTIFICATION_CHANNEL_ID = "counter_channel"
    }
}

