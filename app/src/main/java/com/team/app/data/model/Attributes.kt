package com.team.app.data.model

import androidx.compose.runtime.MutableState
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attributes(
    @Json(name = "coins") val coins: Int,
    @Json(name = "hunger") val hunger: Int, // 0 - 100
    @Json(name = "happiness") val happiness: Int, // 0 - 100
    @Json(name = "health") val health: Int, // 0 - 100
)
