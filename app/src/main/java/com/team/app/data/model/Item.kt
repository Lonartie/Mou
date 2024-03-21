package com.team.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "itemType") val itemType: ItemType,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Int,
    @Json(name = "actionValue") val actionValue: Int
)

@JsonClass(generateAdapter = false)
enum class ItemType {
    @Json(name = "food") FOOD,
    @Json(name = "toy") TOY,
    @Json(name = "medicine") MEDICINE,
    @Json(name = "misc") MISC,
}
