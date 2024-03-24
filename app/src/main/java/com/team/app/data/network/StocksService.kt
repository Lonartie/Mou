package com.team.app.data.network

import com.team.app.data.model.StockData
import com.team.app.data.model.StockPrice
import com.team.app.data.model.StockTimeSeries
import com.team.app.data.model.SymbolData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksService {
    @GET("stocks")
    suspend fun stocks(): Response<StockData>

    @GET("symbol_search")
    suspend fun searchSymbol(
        @Query("symbol") symbol: String,
        @Query("outputsize") size: Int
    ): Response<SymbolData>

    @GET("price")
    suspend fun getPrice(
        @Query("symbol") symbol: String
    ): Response<StockPrice>

    @GET("time_series")
    suspend fun getTimeSeries(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("outputsize") size: Int
    ): Response<StockTimeSeries>
}