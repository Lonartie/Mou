package com.team.app.data.repositories

import android.util.Log
import com.team.app.data.database.StartTimestampDao
import com.team.app.data.database.StepsDao
import com.team.app.data.database.model.StartTimestamp
import com.team.app.data.database.model.StepCountData
import com.team.app.service.StepCounterService
import com.team.app.utils.Constants
import javax.inject.Inject
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

        val stepCountData = StepCountData(
            steps = steps,
            lastLoginId = lastLogin.id
        )

        stepsDao.insertAll(stepCountData)
        return steps.toInt()

    }

    suspend fun insertStartTimestamp() {
        val timestamp = System.currentTimeMillis()
        Log.d(Constants.STEP_COUNTER_TAG, "Adding timestamp: $timestamp")
        timestampsDao.insert(StartTimestamp(0, timestamp, stepCounterService.steps()))
    }

    suspend fun getStepsSinceStart(): Long {
        val lastLogin = timestampsDao.getLast()
        val data = stepsDao.getByLoginId(lastLogin.id)

        if (data.isEmpty())
            return 0

        var sum = 0L
        for (i in 0 until data.size - 1) {
            if (data[i].steps > data[i + 1].steps) {
                sum += data[i].steps
            }
        }
        sum += data.last().steps

        return sum - lastLogin.stepcount
    }

    suspend fun getStepCountDataSince(beginTime: Long): List<StepCountDataModel> {
        return stepsDao
            .getStepCountDataSince(beginTime)
            .map { StepCountDataModel(it.steps, it.timestamp) }
    }

}