package com.team.app.ui.investment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.madrapps.plot.line.DataPoint
import com.team.app.data.model.StockTimeSeries
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class InvestmentPageViewModel @Inject constructor(
    private val stocksRepo: StocksRepository,
    private val investmentsRepo: InvestmentsRepository,
) : ViewModel() {

    private var symbol = ""
    private var timeSeriesAxisData = emptyList<String>()
    val currentCategory = mutableStateOf(StocksRepository.TimeSeriesCategory.YEAR)
    val currentTimeSeries = mutableStateOf(emptyList<DataPoint>())

    suspend fun init(symbol: String) {
        this.symbol = symbol
        updateTimeSeries()
    }

    fun indexMapper(i: Float): String {
        if (i < 0 || i >= timeSeriesAxisData.size) return ""
        return timeSeriesAxisData[i.toInt()]
    }

    suspend fun changeCategory(category: StocksRepository.TimeSeriesCategory) {
        currentCategory.value = category
        updateTimeSeries()
    }

    suspend fun updateTimeSeries() {
        val timeSeries = stocksRepo.getTimeSeries(symbol, currentCategory.value)
        when (currentCategory.value) {
            StocksRepository.TimeSeriesCategory.DAY -> transformDay(timeSeries)
            StocksRepository.TimeSeriesCategory.WEEK -> transformWeek(timeSeries)
            StocksRepository.TimeSeriesCategory.MONTH -> transformMonth(timeSeries)
            StocksRepository.TimeSeriesCategory.YEAR -> transformYear(timeSeries)
        }
    }

    private fun transformDay(timeSeries: StockTimeSeries) {
        val data: MutableList<String> = mutableListOf()
        currentTimeSeries.value = timeSeries.values.mapIndexed { i, sv ->
            val time = LocalDateTime.parse(
                sv.datetime,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val timeStr = time.hour.toString()
            data.add(timeStr)
            DataPoint((timeSeries.values.size - 1 - i).toFloat(), sv.close.toFloat())
        }.reversed()
        timeSeriesAxisData = data.reversed()
    }

    private fun transformWeek(timeSeries: StockTimeSeries) {
        val data: MutableList<String> = mutableListOf()
        currentTimeSeries.value = timeSeries.values.mapIndexed { i, sv ->
            val time = LocalDate.parse(sv.datetime)
            val timeStr = time.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.SHORT,
                java.util.Locale.US
            )
            data.add(timeStr)
            DataPoint((timeSeries.values.size - 1 - i).toFloat(), sv.close.toFloat())
        }.reversed()
        timeSeriesAxisData = data.reversed()
    }

    private fun transformMonth(timeSeries: StockTimeSeries) {
        val data: MutableList<String> = mutableListOf()
        currentTimeSeries.value = timeSeries.values.mapIndexed { i, sv ->
            val time = LocalDate.parse(sv.datetime)
            val timeStr = time.dayOfMonth.toString()
            data.add(timeStr)
            DataPoint((timeSeries.values.size - 1 - i).toFloat(), sv.close.toFloat())
        }.reversed()
        timeSeriesAxisData = data.reversed()
    }

    private fun transformYear(timeSeries: StockTimeSeries) {
        val data: MutableList<String> = mutableListOf()
        currentTimeSeries.value = timeSeries.values.mapIndexed { i, sv ->
            val time = LocalDate.parse(sv.datetime)
            val timeStr = time.monthValue.toString()
            data.add(timeStr)
            DataPoint((timeSeries.values.size - 1 - i).toFloat(), sv.close.toFloat())
        }.reversed()
        timeSeriesAxisData = data.reversed()
    }
}