package com.team.app.data.model

data class Investment (
    val id: Int,
    val symbol: String,
    val type: String,
    val amount: Double,
    val price: Double,
    val leverage: Double,
    val date: Long
)