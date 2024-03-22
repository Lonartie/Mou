package com.team.app

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
        val myWork = PeriodicWorkRequestBuilder<StepCounterWorker>(
            15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).
                enqueueUniquePeriodicWork("StepCounterWorker",
                    ExistingPeriodicWorkPolicy.UPDATE, myWork)
        Log.d("AppApplication", "WorkManager started")
    }
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }

}