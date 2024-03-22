package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attributes")
data class Attributes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coins: Int,
    val hunger: Int,
    val happiness: Int,
    val health: Int
)