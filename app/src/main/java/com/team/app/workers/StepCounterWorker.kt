package com.team.app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.app.data.repositories.StepCounterRepository
import com.team.app.service.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "StepCounterWorker"
@HiltWorker
class StepCounterWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: StepCounterRepository,
    val notificationService: NotificationService,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting worker...")
        val walkedSteps = repository.getStepsSinceStart()
        notificationService.showNotification("You walked  $walkedSteps steps")

        val steps = repository.insertStepCount()
        if (steps == 0) {
            Log.d(TAG, "No new steps since last termination")
            return Result.success()
        }


        Log.d(TAG, "Stopping worker...")
        return Result.success()
    }

}