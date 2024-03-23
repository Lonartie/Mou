package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.app.data.database.model.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(item: InventoryItem)

    @Delete
    suspend fun removeItem(item: InventoryItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: InventoryItem)

    @Query("SELECT * FROM inventory")
    suspend fun getItems(): List<InventoryItem>

    @Query("SELECT * FROM inventory")
    fun getItemsFlow(): Flow<InventoryItem>

    //@Query("SELECT * FROM inventory WHERE ")

    @Query("SELECT * FROM inventory WHERE id = :id")
    suspend fun getItem(id: Int): InventoryItem?

    @Query("SELECT id FROM inventory WHERE itemID = :itemID")
    suspend fun findByItemID(itemID: Int): Int?
}