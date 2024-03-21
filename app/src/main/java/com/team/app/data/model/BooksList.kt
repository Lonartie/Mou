package com.lonartie.bookdiary.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksList (
    @Json(name = "totalItems") val totalItems: Int,
    @Json(name = "items") val items: List<Book>
)