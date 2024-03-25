package com.team.app.data.repositories

import com.team.app.data.model.StockTimeSeries
import com.team.app.data.model.Symbol
import com.team.app.data.network.StocksService

class StocksRepository(
    private val stocksService: StocksService
) {
    enum class TimeSeriesCategory {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }

    suspend fun getAllStocks() = stocksService.stocks().body()!!.data

    suspend fun searchSymbol(symbol: String, size: Int = 30): List<Symbol> {
        return stocksService
            .searchSymbol(symbol, size).body()!!.data
            .filter {
                it.currency == "USD" || it.symbol.endsWith("/USD")
            }
    }

    suspend fun getPrice(symbol: String): Double {
        return stocksService.getPrice(symbol).body()!!.price.toDoubleOrNull() ?: 0.0
    }

    suspend fun getTimeSeries(symbol: String, category: TimeSeriesCategory): StockTimeSeries {
        val (interval, size) = when (category) {
            TimeSeriesCategory.DAY -> Pair("2h", 12)
            TimeSeriesCategory.WEEK -> Pair("1day", 7)
            TimeSeriesCategory.MONTH -> Pair("1day", 31)
            TimeSeriesCategory.YEAR -> Pair("1month", 12)
        }

        return stocksService.getTimeSeries(symbol, interval, size).body()!!
    }
}