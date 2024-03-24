package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investments")
data class Investment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String,
    val type: String,
    val amount: Double,
    val price: Double,
    val date: Long,
)
