package com.team.app.ui.investment

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.lineSeries
import com.team.app.data.model.Investment
import com.team.app.data.model.StockTimeSeries
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.NetworkRepository
import com.team.app.data.repositories.StocksRepository
import com.team.app.utils.earningsFromSell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    private val attributesRepo: AttributesRepository,
    private val investmentsRepo: InvestmentsRepository,
    networkRepo: NetworkRepository
) : ViewModel() {

    private var symbol = ""
    private var type = ""
    private var stocksData = mapOf<StocksRepository.TimeSeriesCategory, StockTimeSeries?>()
    val currentPrice = mutableDoubleStateOf(0.0)
    val currentCategory = mutableStateOf(StocksRepository.TimeSeriesCategory.DAY)
    val modelProducer = CartesianChartModelProducer.build()
    val xAxisKey = ExtraStore.Key<List<String>>()
    val minMaxKey = ExtraStore.Key<Pair<Float, Float>>()
    val networkStatus = networkRepo.networkStatus
    val balance: Flow<Int> = attributesRepo.getAttributesFlow().map {it.coins}
    var investmentsValue: Flow<Double>? = null
    var investments: Flow<List<Investment>>? = null
    val doneLoading = mutableStateOf(false)

    suspend fun init(symbol: String) {
        doneLoading.value = false
        this.symbol = symbol
        if (networkStatus.value.not()) return
        this.currentPrice.doubleValue = stocksRepo.getPrice(symbol)
        this.investmentsValue = investmentsRepo.getBalanceFlow(symbol, currentPrice.doubleValue)
        this.investments = investmentsRepo.getInvestmentsFlow(symbol)
        val stocksData = mapOf(
            StocksRepository.TimeSeriesCategory.DAY to null,
            StocksRepository.TimeSeriesCategory.WEEK to null,
            StocksRepository.TimeSeriesCategory.MONTH to null,
            StocksRepository.TimeSeriesCategory.YEAR to null
        )

        this.stocksData = stocksData
        updateTimeSeries()
        doneLoading.value = true
    }

    suspend fun buy(coinsStr: String, leverageStr: String): String {
        if (type.isEmpty()) {
            return "Please wait for data to load"
        }

        var coins = coinsStr.toIntOrNull() ?: 0
        val leverage = leverageStr.toIntOrNull() ?: 0

        val currentAttributes = attributesRepo.getAttributes()
        if (currentAttributes.coins < coins) {
            return "Not enough coins"
        }
        if (coins <= 5) {
            return "Enter amount of coins >= 5"
        }
        if (0 >= leverage || leverage > 100) {
            return "Leverage must be between 1 and 100"
        }
        coins -= 5 // fee
        attributesRepo.updateCoins(currentAttributes.coins - coins)
        val investment = Investment(
            id = 0,
            symbol = symbol,
            price = currentPrice.doubleValue,
            amount = coins.toDouble(),
            leverage = leverage.toDouble(),
            date = System.currentTimeMillis(),
            type = type
        )
        investmentsRepo.addInvestment(investment)
        return "Invested $coins coins in $symbol"
    }

    suspend fun onSell(inv: Investment): String {
        val currentAttributes = attributesRepo.getAttributes()
        val balanceAfter = inv.earningsFromSell(currentPrice.doubleValue)
        attributesRepo.updateCoins(currentAttributes.coins + balanceAfter)
        investmentsRepo.removeInvestment(inv)
        return "Earned $balanceAfter coins from selling $symbol"
    }

    suspend fun changeCategory(category: StocksRepository.TimeSeriesCategory) {
        currentCategory.value = category
        updateTimeSeries()
    }

    private suspend fun updateTimeSeries() {
        if (stocksData[currentCategory.value] == null) {
            doneLoading.value = false
            val data = stocksRepo.getTimeSeries(symbol, currentCategory.value)
            doneLoading.value = true
            stocksData = stocksData + mapOf(currentCategory.value to data)
        }

        val timeSeries =  stocksData[currentCategory.value] ?: return

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