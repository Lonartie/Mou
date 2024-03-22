package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.app.data.model.ItemType

@Entity(tableName = "items")
data class OwnedItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: ItemType
)