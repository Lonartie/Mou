package com.team.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockPrice(
    @Json(name = "price") val price: String,
)
