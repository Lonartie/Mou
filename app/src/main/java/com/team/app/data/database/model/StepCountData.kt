package com.team.app.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepCountData (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val steps: Long,
    @ColumnInfo val lastLoginId: Int,
    @ColumnInfo val timestamp: Long = System.currentTimeMillis()
)
