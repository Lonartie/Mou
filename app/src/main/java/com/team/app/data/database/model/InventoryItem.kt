package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory"
)
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemID: Int,
    val quantity: Int
)