package com.team.app.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat
import com.team.app.MainActivity
import com.team.app.R
import okhttp3.internal.notify

class NotificationService(
    private val context: Context,
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotification(messgae : String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_fastfood_24)
            .setContentTitle("Food time Notification")
            .setContentText(messgae)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        notificationManager.notify(
            2, notification
        )


    }
    companion object{
        const val NOTIFICATION_CHANNEL_ID = "channel"
    }
}

