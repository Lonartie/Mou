package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team.app.data.database.model.StepCountData

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps")
    suspend fun getAll(): List<StepCountData>

    @Insert
    suspend fun insertAll(vararg steps: StepCountData)

    @Delete
    suspend fun delete(steps: StepCountData)

    @Query("SELECT * FROM steps ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): StepCountData

    // get Steps with login id
    @Query("SELECT * FROM steps WHERE lastLoginId = :id")
    suspend fun getByLoginId(id: Int): List<StepCountData>

    @Query("DELETE FROM steps")
    suspend fun deleteAll()

    @Query("SELECT * FROM steps WHERE timestamp >= :timestamp")
    suspend fun getStepCountDataSince(timestamp: Long): List<StepCountData>
}
