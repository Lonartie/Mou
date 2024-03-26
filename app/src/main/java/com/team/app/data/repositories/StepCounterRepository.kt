package com.team.app.data.repositories

import android.util.Log
import com.team.app.data.database.StartTimestampDao
import com.team.app.data.database.StepsDao
import com.team.app.data.database.model.StartTimestamp
import com.team.app.data.database.model.StepCountData
import com.team.app.service.StepCounterService
import com.team.app.utils.Constants
import javax.inject.Inject
import kotlin.math.max
import com.team.app.data.model.StepCountData as StepCountDataModel

class StepCounterRepository @Inject constructor(
    private val stepCounterService: StepCounterService,
    private val stepsDao: StepsDao,
    private val timestampsDao: StartTimestampDao,
) {

    suspend fun insertFirstStartTimestamp() {
        if (timestampsDao.getAll().isEmpty()) {
            val timestamp = System.currentTimeMillis()
            Log.d(Constants.STEP_COUNTER_TAG, "Adding first timestamp: $timestamp")
            timestampsDao.insert(StartTimestamp(0, timestamp, 0))
        }
    }

    suspend fun insertStepCount(): Int {
        val steps = stepCounterService.steps()
        val lastLogin = timestampsDao.getLast()
        val lastSteps = stepsDao.getByLoginId(lastLogin.id)

        if (lastSteps.isEmpty()) {
            val stepCountData = StepCountData(
                steps = steps - lastLogin.stepcount,
                lastLoginId = lastLogin.id
            )
            Log.d(Constants.STEP_COUNTER_TAG, "Adding step count: $stepCountData")
            stepsDao.insertAll(stepCountData)
            return stepCountData.steps.toInt()
        } else {
            val maxSteps = lastSteps.maxOf { it.steps }
            val difference = max(0, (steps - lastLogin.stepcount) - maxSteps.toInt())
            val stepCountData = StepCountData(
                steps = difference,
                lastLoginId = lastLogin.id
            )
            Log.d(Constants.STEP_COUNTER_TAG, "Adding step count: $stepCountData")
            stepsDao.insertAll(stepCountData)
            return stepCountData.steps.toInt()
        }
    }

    suspend fun insertStartTimestamp() {
        val timestamp = System.currentTimeMillis()
        Log.d(Constants.STEP_COUNTER_TAG, "Adding timestamp: $timestamp")
        timestampsDao.insert(StartTimestamp(0, timestamp, stepCounterService.steps()))
    }

    suspend fun getStepsSinceStart(): Long {
        val lastLoginId = timestampsDao.getLast().id
        val data = stepsDao.getByLoginId(lastLoginId)
        if (data.isEmpty()) {
            return 0
        }
        return data.sumOf { it.steps }
    }

    suspend fun getStepCountDataSince(beginTime: Long): List<StepCountDataModel> {
        return stepsDao
            .getStepCountDataSince(beginTime)
            .map { StepCountDataModel(it.steps, it.timestamp) }
    }

}