package com.team.app.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "start_timestamp")
data class StartTimestamp (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val stepcount: Long,
)