package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "hotbar",
    foreignKeys = [ForeignKey(
        entity = InventoryItem::class,
        parentColumns = ["id"],
        childColumns = ["food"]
    ), ForeignKey(
        entity = InventoryItem::class,
        parentColumns = ["id"],
        childColumns = ["toy"]
    ), ForeignKey(
        entity = InventoryItem::class,
        parentColumns = ["id"],
        childColumns = ["misc"]
    )]
)
data class HotbarItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val food: Int,
    val toy: Int,
    val misc: Int
)