package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.Investment

@Database(entities = [Investment::class], version = 1)
abstract class InvestmentsDatabase : RoomDatabase() {
    abstract fun investmentsDao(): InvestmentsDao
}