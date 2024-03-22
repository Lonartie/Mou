package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.app.data.database.model.HotbarItem
import kotlinx.coroutines.flow.Flow


@Dao
interface HotbarDao {
    @Delete
    suspend fun deleteItem(item: HotbarItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(item: HotbarItem)

    @Query("SELECT * FROM hotbar")
    fun getItemsFlow(): Flow<HotbarItem>

    @Query("SELECT * FROM hotbar")
    suspend fun getItems(): List<HotbarItem>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: HotbarItem)
}