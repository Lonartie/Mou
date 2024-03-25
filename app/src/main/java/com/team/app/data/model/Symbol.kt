package com.team.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Symbol (
    @Json(name = "symbol") val symbol: String,
    @Json(name = "instrument_name") val instrumentName: String,
    @Json(name = "country") val country: String,
    @Json(name = "instrument_type") val instrumentType: String,
    @Json(name = "currency") val currency: String
)

@JsonClass(generateAdapter = true)
data class SymbolData(
    @Json(name = "data") val data: List<Symbol>
)