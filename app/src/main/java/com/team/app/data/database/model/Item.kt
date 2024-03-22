package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: Int,
    val name: String,
    val price: Int,
    val actionValue: Int
)