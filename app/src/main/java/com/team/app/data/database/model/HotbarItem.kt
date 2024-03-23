package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "hotbar",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = InventoryItem::class,
            parentColumns = ["id"],
            childColumns = ["food"],
            onUpdate = androidx.room.ForeignKey.CASCADE,
        ),
        androidx.room.ForeignKey(
            entity = InventoryItem::class,
            parentColumns = ["id"],
            childColumns = ["toy"],
            onUpdate = androidx.room.ForeignKey.CASCADE,
        ),
        androidx.room.ForeignKey(
            entity = InventoryItem::class,
            parentColumns = ["id"],
            childColumns = ["misc"],
            onUpdate = androidx.room.ForeignKey.CASCADE,
        )
    ]
)
data class HotbarItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val food: Int,
    val toy: Int,
    val misc: Int
)