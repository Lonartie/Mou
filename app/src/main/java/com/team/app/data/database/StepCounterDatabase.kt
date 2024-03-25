package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.StartTimestamp
import com.team.app.data.database.model.StepCountData

@Database(entities = [StepCountData::class, StartTimestamp::class], version = 1)
abstract class StepCounterDatabase : RoomDatabase() {
    abstract fun stepsDao(): StepsDao
    abstract fun startTimestampDao(): StartTimestampDao
}