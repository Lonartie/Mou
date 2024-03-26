package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team.app.data.database.model.StartTimestamp

@Dao
interface StartTimestampDao {

    @Insert
    suspend fun insert(startTimestamp: StartTimestamp)

    @Query("SELECT * FROM start_timestamp")
    suspend fun getAll(): List<StartTimestamp>

    @Query("SELECT * FROM start_timestamp WHERE id = :id")
    suspend fun getById(id: Int): StartTimestamp

    @Query("SELECT * FROM start_timestamp ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): StartTimestamp

}