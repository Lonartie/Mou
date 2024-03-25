package com.team.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockTimeSeries(
    @Json(name = "meta") val meta: Meta,
    @Json(name = "values") val values: List<StockValue>
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "interval") val interval: String,
    @Json(name = "currency") val currency: String? = null,
    @Json(name = "type") val type: String
)

@JsonClass(generateAdapter = true)
data class StockValue(
    @Json(name = "datetime") val datetime: String,
    @Json(name = "close") val close: String
)
