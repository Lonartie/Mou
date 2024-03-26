package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotbar")
data class HotbarItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val food: Int,
    val toy: Int,
    val misc: Int
)