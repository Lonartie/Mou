package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.StepCountData

@Database(entities = [StepCountData::class], version = 1)
abstract class StepCounterDatabase : RoomDatabase() {
    abstract fun stepsDao(): StepsDao
}