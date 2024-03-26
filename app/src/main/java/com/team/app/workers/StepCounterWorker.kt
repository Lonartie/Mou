package com.team.app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.StepCounterRepository
import com.team.app.ui.common.Notification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class StepCounterWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: StepCounterRepository,
    private val notificationService: Notification,
    private val attributesRepository: AttributesRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        private const val TAG = "StepCounterWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting worker...")
        coroutineScope {
            val walkedSteps: Long = repository.getStepsSinceStart()
            sendWalkedStepsNotification(walkedSteps)
        }

        coroutineScope {
            val steps = repository.insertStepCount()
            if (steps == 0) {
                Log.d(TAG, "No new steps since last termination")
            }
        }

        coroutineScope {
            decreaseAttributes()
        }

        Log.d(TAG, "Stopping worker...")
        return Result.success()
    }

    private fun sendWalkedStepsNotification(steps: Long) {
        if (steps == 0L) return
        notificationService.showNotification("You walked  $steps steps")
    }

    private suspend fun decreaseAttributes() {
        // Finde eine methode, die die Attribute prozentual verringert, aber immer um einen Sockelwert
        val attr = attributesRepository.getAttributes()
        val newHunger = attr.hunger - attr.hunger * 0.25
        val newHappiness = attr.happiness - attr.happiness * 0.25
        val newHealth = attr.health - attr.health * 0.25
        attributesRepository.updateHunger(newHunger.toInt())
        attributesRepository.updateHappiness(newHappiness.toInt())
        attributesRepository.updateHealth(newHealth.toInt())
    }

}