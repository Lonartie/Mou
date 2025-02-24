package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.app.data.database.model.Item
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(item: Item)

    @Delete
    suspend fun removeItem(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items")
    suspend fun getItems(): List<Item>

    @Query("SELECT * FROM items")
    fun getItemsFlow(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE name != '' ORDER BY type, name")
    fun getSortedItemsNoInvalidFlow(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Int): Item?

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemFlow(id: Int): Flow<Item>?

    @Query("SELECT id FROM items WHERE name = :name")
    suspend fun findByName(name: String): Int?
}