package com.team.app.data.repositories

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.team.app.data.database.StepsDao
import com.team.app.data.database.model.StepCountData
import com.team.app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.resume
class StepCounterRepository @Inject constructor(
    val sensorManager: SensorManager,
    val stepsDao : StepsDao,
    ) {
    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    suspend fun steps() = suspendCancellableCoroutine { continuation ->
        Log.d(TAG, "Registering sensor listener... ")

        val listener: SensorEventListener by lazy {
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return

                    val stepsSinceLastReboot = event.values[0].toLong()
                    Log.d(TAG, "Steps since last reboot: $stepsSinceLastReboot")

                    sensorManager.unregisterListener(this)
                    Log.d(TAG, "Sensor listener unregistered")

                    if (continuation.isActive) {
                        continuation.resume(stepsSinceLastReboot)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    Log.d(TAG, "Accuracy changed to: $accuracy")
                }
            }
        }
        if (stepCounterSensor != null) {
            val supportedAndEnabled = sensorManager
                .registerListener(
                    listener,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_UI)
            Log.d(TAG, "Sensor listener registered: $supportedAndEnabled")
        }
    }

    suspend fun storeSteps(steps: Long) = withContext(Dispatchers.IO) {
        val stepCountData = StepCountData(
            steps = steps,
            createdAt = Instant.now().toString()
        )
        Log.d(Constants.STEP_COUNTER_TAG, "Storing steps: $stepCountData")
        stepsDao.insertAll(stepCountData)
    }

    suspend fun loadStepsSinceTerminate() : Long = withContext(Dispatchers.IO) {
        val data = stepsDao.getAll()
        when {
            data.isEmpty() -> 0
            else -> data.sumOf { it.steps }
        }
    }

    suspend fun clearSteps() = withContext(Dispatchers.IO) {
        stepsDao.deleteAll()
    }

    suspend fun getMaxSteps() : Long = withContext(Dispatchers.IO) {
        val data = stepsDao.getAll()
        when {
            data.isEmpty() -> 0
            else -> data.maxOf { it.steps }
        }
    }

    suspend fun addSteps() {
        val steps = steps() - getMaxSteps()
        storeSteps(steps)
    }

}