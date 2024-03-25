package com.team.app.ui.investment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.lineSeries
import com.team.app.data.model.StockTimeSeries
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class InvestmentPageViewModel @Inject constructor(
    private val stocksRepo: StocksRepository,
    private val investmentsRepo: InvestmentsRepository,
) : ViewModel() {

    private var symbol = ""
    private var stocksData = mapOf<StocksRepository.TimeSeriesCategory, StockTimeSeries>()
    val currentCategory = mutableStateOf(StocksRepository.TimeSeriesCategory.DAY)
    val modelProducer = CartesianChartModelProducer.build()
    val xAxisKey = ExtraStore.Key<List<String>>()
    val minMaxKey = ExtraStore.Key<Pair<Float, Float>>()

    suspend fun init(symbol: String) {
        this.symbol = symbol
        val stocksData = mapOf(
            StocksRepository.TimeSeriesCategory.DAY to
                    stocksRepo.getTimeSeries(
                        symbol,
                        StocksRepository.TimeSeriesCategory.DAY
                    ),
            StocksRepository.TimeSeriesCategory.WEEK to
                    stocksRepo.getTimeSeries(
                        symbol,
                        StocksRepository.TimeSeriesCategory.WEEK
                    ),
            StocksRepository.TimeSeriesCategory.MONTH to
                    stocksRepo.getTimeSeries(
                        symbol,
                        StocksRepository.TimeSeriesCategory.MONTH
                    ),
            StocksRepository.TimeSeriesCategory.YEAR to
                    stocksRepo.getTimeSeries(
                        symbol,
                        StocksRepository.TimeSeriesCategory.YEAR
                    )
        )
        this.stocksData = stocksData
        updateTimeSeries()
    }

    suspend fun changeCategory(category: StocksRepository.TimeSeriesCategory) {
        currentCategory.value = category
        updateTimeSeries()
    }

    private suspend fun updateTimeSeries() {
        val timeSeries = stocksData[currentCategory.value] ?: return
        when (currentCategory.value) {
            StocksRepository.TimeSeriesCategory.DAY -> transformDay(timeSeries)
            StocksRepository.TimeSeriesCategory.WEEK -> transformWeek(timeSeries)
            StocksRepository.TimeSeriesCategory.MONTH -> transformMonth(timeSeries)
            StocksRepository.TimeSeriesCategory.YEAR -> transformYear(timeSeries)
        }
    }

    private suspend fun transformDay(timeSeries: StockTimeSeries) {
        val xValues = mutableListOf<String>()
        val yValues = mutableListOf<Number>()

        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE
        timeSeries.values
            .reversed()
            .forEach {
                val date = LocalDateTime.parse(
                    it.datetime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
                val xValue = date.format(DateTimeFormatter.ofPattern("HH:mm"))
                val yValue = it.close.toFloat()
                xValues.add(xValue)
                yValues.add(yValue)
                minY = min(minY, yValue)
                maxY = max(maxY, yValue)
            }
        val range = maxY - minY
        minY -= (range * 0.1f)
        maxY += (range * 0.1f)
        modelProducer.tryRunTransaction {
            lineSeries { series(yValues) }
            updateExtras {
                it[xAxisKey] = xValues
                it[minMaxKey] = minY to maxY
            }
        }
    }

    private suspend fun transformWeek(timeSeries: StockTimeSeries) {
        val xValues = mutableListOf<String>()
        val yValues = mutableListOf<Number>()

        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE

        timeSeries.values
            .reversed()
            .forEach {
                val date = LocalDate.parse(it.datetime)
                val xValue = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
                val yValue = it.close.toFloat()
                xValues.add(xValue)
                yValues.add(yValue)
                minY = min(minY, yValue)
                maxY = max(maxY, yValue)
            }
        val range = maxY - minY
        minY -= (range * 0.1f)
        maxY += (range * 0.1f)
        modelProducer.tryRunTransaction {
            lineSeries { series(yValues) }
            updateExtras {
                it[xAxisKey] = xValues
                it[minMaxKey] = minY to maxY
            }
        }
    }

    private suspend fun transformMonth(timeSeries: StockTimeSeries) {
        val xValues = mutableListOf<String>()
        val yValues = mutableListOf<Number>()

        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE

        timeSeries.values
            .reversed()
            .forEach {
                val date = LocalDate.parse(it.datetime)
                val xValue = date.format(DateTimeFormatter.ofPattern("dd. MMM", Locale.US))
                val yValue = it.close.toFloat()
                xValues.add(xValue)
                yValues.add(yValue)
                minY = min(minY, yValue)
                maxY = max(maxY, yValue)
            }
        val range = maxY - minY
        minY -= (range * 0.1f)
        maxY += (range * 0.1f)
        modelProducer.tryRunTransaction {
            lineSeries { series(yValues) }
            updateExtras {
                it[xAxisKey] = xValues
                it[minMaxKey] = minY to maxY
            }
        }
    }

    private suspend fun transformYear(timeSeries: StockTimeSeries) {
        val xValues = mutableListOf<String>()
        val yValues = mutableListOf<Number>()

        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE

        timeSeries.values
            .reversed()
            .forEach {
                val date = LocalDate.parse(it.datetime)
                val xValue = date.month.getDisplayName(TextStyle.SHORT, Locale.US)
                val yValue = it.close.toFloat()
                xValues.add(xValue)
                yValues.add(yValue)
                minY = min(minY, yValue)
                maxY = max(maxY, yValue)
            }
        val range = maxY - minY
        minY -= (range * 0.1f)
        maxY += (range * 0.1f)
        modelProducer.tryRunTransaction {
            lineSeries { series(yValues) }
            updateExtras {
                it[xAxisKey] = xValues
                it[minMaxKey] = minY to maxY
            }
        }
    }
}