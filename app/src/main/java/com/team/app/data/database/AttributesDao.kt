package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.team.app.data.database.model.Attributes
import kotlinx.coroutines.flow.Flow

@Dao
interface AttributesDao {
    @Delete
    suspend fun deleteAttributes(attributes: Attributes)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAttributes(attributes: Attributes)

    @Query("SELECT * FROM attributes")
    fun getAttributesFlow(): Flow<Attributes>

    @Query("SELECT * FROM attributes")
    suspend fun getAttributes(): List<Attributes>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAttributes(attributes: Attributes)
}