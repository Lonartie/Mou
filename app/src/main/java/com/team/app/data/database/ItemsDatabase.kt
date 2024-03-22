package com.team.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.app.data.database.model.HotbarItem
import com.team.app.data.database.model.InventoryItem
import com.team.app.data.database.model.Item

@Database(entities = [Item::class, InventoryItem::class, HotbarItem::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun hotbarDao(): HotbarDao
}