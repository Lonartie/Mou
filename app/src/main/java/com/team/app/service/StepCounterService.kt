package com.team.app.service

import android.content.ContentValues
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class StepCounterService @Inject constructor(
    private val sensorManager: SensorManager
) {
    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    suspend fun steps() = suspendCancellableCoroutine { continuation ->
        Log.d(ContentValues.TAG, "Registering sensor listener... ")

        val listener: SensorEventListener by lazy {
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return

                    val stepsSinceLastReboot = event.values[0].toLong()
                    Log.d(ContentValues.TAG, "Steps since last reboot: $stepsSinceLastReboot")

                    sensorManager.unregisterListener(this)
                    Log.d(ContentValues.TAG, "Sensor listener unregistered")

                    if (continuation.isActive) {
                        continuation.resume(stepsSinceLastReboot)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    Log.d(ContentValues.TAG, "Accuracy changed to: $accuracy")
                }
            }
        }
        if (stepCounterSensor != null) {
            val supportedAndEnabled = sensorManager
                .registerListener(
                    listener,
                    stepCounterSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            Log.d(ContentValues.TAG, "Sensor listener registered: $supportedAndEnabled")
        }
    }
}