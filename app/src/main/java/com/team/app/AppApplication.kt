package com.team.app

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.team.app.service.NotificationService
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppApplication : Application(), Configuration.Provider {

    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {

        super.onCreate()
        createNotifationChannel()
    }


    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }
    @SuppressLint("ObsoleteSdkInt")
    private fun createNotifationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel (
                NotificationService.NOTIFICATION_CHANNEL_ID,
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Reminder to feed me!"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}