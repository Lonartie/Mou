package com.team.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.app.data.repositories.AttributesRepository
import com.team.app.ui.common.Notification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DecreaseAttributesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val attributesRepo: AttributesRepository,
    private val notificationService: Notification,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // worker works every 15 minutes
        val callsPerDay = 24 * (60 / 15)

        val decreaseHunger = 3
        val decreaseHappiness = 2
        val decreaseHealth = 3

        val currentAttributes = attributesRepo.getAttributes()
        val newAttributes = currentAttributes.copy(
            hunger = (currentAttributes.hunger - decreaseHunger)
                .coerceAtLeast(0),
            happiness = (currentAttributes.happiness - decreaseHappiness)
                .coerceAtLeast(0),
            health =
            if (currentAttributes.hunger == 0 && currentAttributes.happiness == 0)
                (currentAttributes.health - decreaseHealth)
                    .coerceAtLeast(0)
            else
                currentAttributes.health
        )

        attributesRepo.updateHunger(newAttributes.hunger)
        attributesRepo.updateHappiness(newAttributes.happiness)
        attributesRepo.updateHealth(newAttributes.health)

        if (newAttributes.health == 0) {
            notificationService.showNotification("Your pet has died!")
        } else if (newAttributes.health < 50) {
            notificationService.showNotification("Your pet is sick!")
        } else if (newAttributes.happiness < 50 && newAttributes.hunger < 50) {
            notificationService.showNotification("Your pet is unhappy and hungry!")
        } else if (newAttributes.happiness < 50) {
            notificationService.showNotification("Your pet is unhappy!")
        } else if (newAttributes.hunger < 50) {
            notificationService.showNotification("Your pet is hungry!")
        }

        return Result.success()
    }
}