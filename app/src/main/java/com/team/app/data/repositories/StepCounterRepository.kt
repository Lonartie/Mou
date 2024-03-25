package com.team.app.data.repositories

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.team.app.data.database.StartTimestampDao
import com.team.app.data.database.StepsDao
import com.team.app.data.database.model.StartTimestamp
import com.team.app.data.database.model.StepCountData
import com.team.app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.max

class StepCounterRepository @Inject constructor(
    val sensorManager: SensorManager,
    val stepsDao : StepsDao,
    val timestampsDao : StartTimestampDao,
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

    suspend fun insertFirstStartTimestamp() {
        if (timestampsDao.getAll().isEmpty()) {
            val timestamp = System.currentTimeMillis()
            Log.d(Constants.STEP_COUNTER_TAG, "Adding first timestamp: $timestamp")
            timestampsDao.insert(StartTimestamp(0, timestamp, 0))
        }
    }

    suspend fun insertStepCount() : Int {
        val steps = steps()
        val lastLogin = timestampsDao.getLast()
        val lastSteps = stepsDao.getByLoginId(lastLogin.id)

        if (lastSteps.isEmpty()) {
            val stepCountData = StepCountData(
                steps = steps - lastLogin.stepcount,
                lastLoginId = lastLogin.id)
            Log.d(Constants.STEP_COUNTER_TAG, "Adding step count: $stepCountData")
            stepsDao.insertAll(stepCountData)
            return stepCountData.steps.toInt()
        } else {
            val maxSteps = lastSteps.maxOf { it.steps }
            val difference = max(0, (steps - lastLogin.stepcount) - maxSteps.toInt())
            val stepCountData = StepCountData(
                steps = difference,
                lastLoginId = lastLogin.id)
            Log.d(Constants.STEP_COUNTER_TAG, "Adding step count: $stepCountData")
            stepsDao.insertAll(stepCountData)
            return stepCountData.steps.toInt()
        }
    }

    suspend fun insertStartTimestamp() {
        val timestamp = System.currentTimeMillis()
        Log.d(Constants.STEP_COUNTER_TAG, "Adding timestamp: $timestamp")
        timestampsDao.insert(StartTimestamp(0, timestamp, steps()))
    }

    suspend fun getStepsSinceStart(): Long {
        val lastLoginId = timestampsDao.getLast().id
        val data = stepsDao.getByLoginId(lastLoginId)
        if (data.isEmpty()) {
            return 0
        }
        return data.sumOf { it.steps }
    }

}