package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.team.app.data.database.model.OwnedItem

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(ownedItem: OwnedItem)

    //@Update
    //suspend fun updateItem(item: Item)

    //@Query("SELECT * FROM items WHERE type = :type")
    //fun getItemByType(type: ItemType): Flow<Item>
}