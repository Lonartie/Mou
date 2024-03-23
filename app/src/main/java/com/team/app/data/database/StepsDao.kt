package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.team.app.data.database.model.StepCountData
import retrofit2.http.DELETE

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps")
    suspend fun getAll(): List<StepCountData>

    @Insert
    suspend fun insertAll(vararg steps: StepCountData)

    @Delete
    suspend fun delete(steps: StepCountData)

    @Query("DELETE FROM steps")
    suspend fun deleteAll()
}
