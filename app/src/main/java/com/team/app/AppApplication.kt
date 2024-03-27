package com.team.app

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.team.app.ui.common.Notification.Companion.NOTIFICATION_CHANNEL_ID
import com.team.app.workers.DecreaseAttributesWorker
import com.team.app.workers.InvestmentWorker
import com.team.app.workers.StepCounterWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class AppApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {

        super.onCreate()
        val stepCounterWorker = PeriodicWorkRequestBuilder<StepCounterWorker>(
            15, TimeUnit.MINUTES).build()
        val attributeWorker = PeriodicWorkRequestBuilder<DecreaseAttributesWorker>(
            15, TimeUnit.MINUTES).build()
        val investmentWorker = PeriodicWorkRequestBuilder<InvestmentWorker>(
            1, TimeUnit.HOURS).build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "StepCounterWorker",
                ExistingPeriodicWorkPolicy.UPDATE, stepCounterWorker
            )
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "AttributeWorker",
                ExistingPeriodicWorkPolicy.UPDATE, attributeWorker
            )
        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "InvestmentWorker",
                ExistingPeriodicWorkPolicy.UPDATE, investmentWorker)
        Log.d("AppApplication", "WorkManager started")
        createNotificationChannel()
    }


    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }
    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Mou - Notification"
            val descriptionText = "When your mini-Me calls for you"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply{
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



}