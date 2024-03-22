package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["itemID"],
            onUpdate = androidx.room.ForeignKey.CASCADE,
        )
    ]
)
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemID: Int,
    val quantity: Int
)