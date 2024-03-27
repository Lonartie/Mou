package com.team.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.StocksRepository
import com.team.app.ui.common.Notification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class InvestmentWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val investmentsRepo: InvestmentsRepository,
    private val stocksRepo: StocksRepository,
    private val notificationService: Notification,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val investments = investmentsRepo
            .getInvestments()

        if (investments.isEmpty())
            return Result.success()

        val percentages = investments
            .groupBy { it.symbol }
            .map { (symbol, investments) ->
                delay(10_000)
                val price = stocksRepo.getPrice(symbol)
                investments.map { it to ((price / it.price - 1) * 100) }
            }.flatten()

        val minPercentage = percentages.minBy { it.second }
        val maxPercentage = percentages.maxBy { it.second }

        println("Min: $minPercentage")
        println("Max: $maxPercentage")

        val message: String
        if (minPercentage.first.symbol == maxPercentage.first.symbol ||
            minPercentage.second == 0.0 || maxPercentage.second == 0.0) {
            message = if (minPercentage.second <= -0.01) {
                "Attention: Your investment in ${minPercentage.first.symbol} " +
                        "has decreased by ${"%.2f".format(minPercentage.second)}%"
            } else if (maxPercentage.second >= 0.01) {
                "Congratulations: Your investment in ${maxPercentage.first.symbol} " +
                        "has increased by ${"%.2f".format(maxPercentage.second)}%"
            } else {
                ""
            }
        } else {
            message = if (minPercentage.second <= -0.01 && maxPercentage.second < 0) {
                "Attention: All your investments have decreased by up to" +
                        " ${"%.2f".format(minPercentage.second)}% " +
                        "(${minPercentage.first.symbol})"
            } else if (minPercentage.second <= -0.01 && maxPercentage.second >= 0.01) {
                "Attention: ${minPercentage.first.symbol} decreased by " +
                        " ${"%.2f".format(minPercentage.second)}% " +
                        " and ${maxPercentage.first.symbol} increased by " +
                        "${"%.2f".format(maxPercentage.second)}%"
            } else if (minPercentage.second >= 0.01 && maxPercentage.second >= 0.01) {
                "Congratulations: All of your investments have increased by up to" +
                        " ${"%.2f".format(maxPercentage.second)}% " +
                        "(${maxPercentage.first.symbol})"
            } else {
                ""
            }
        }

        if (message.isNotEmpty()) {
            notificationService.showNotification(message, "Investment-Updates")
        }

        return Result.success()
    }
}