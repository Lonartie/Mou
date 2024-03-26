package com.team.app.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepCountData (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val steps: Long,
    val lastLoginId: Int,
    val timestamp: Long = System.currentTimeMillis()
)
