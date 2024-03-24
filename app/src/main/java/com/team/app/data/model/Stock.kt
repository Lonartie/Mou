package com.team.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stock(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "country") val country: String,
    @Json(name = "type") val type: String,
)

@JsonClass(generateAdapter = true)
data class StockData(
    @Json(name = "data") val data: List<Stock>
)

