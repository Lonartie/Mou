package com.team.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class InvestmentWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    // add here the dependencies for the worker
)  : CoroutineWorker(appContext, workerParams)  {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}