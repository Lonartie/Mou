package com.team.app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.app.data.repositories.StepCounterRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "StepCounterWorker"
@HiltWorker
class StepCounterWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: StepCounterRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting worker...")

        val stepsSinceLastTermination = repository.steps() - repository.getMaxSteps()
        if (stepsSinceLastTermination == 0L) return Result.success()

        Log.d(TAG, "Received steps from step sensor: $stepsSinceLastTermination")
        repository.storeSteps(stepsSinceLastTermination)

        Log.d(TAG, "Stopping worker...")
        return Result.success()
    }

}