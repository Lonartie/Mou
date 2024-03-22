package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.OwnedItem

@Database(entities = [OwnedItem::class], version = 1)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}