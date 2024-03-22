package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.Attributes

@Database(entities = [Attributes::class], version = 1)
abstract class AttributesDatabase : RoomDatabase() {
    abstract fun attributesDao(): AttributesDao
}