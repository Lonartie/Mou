package com.team.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team.app.data.database.model.Investment
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addInvestmentInternal(investment: Investment)

    suspend fun addInvestment(investment: Investment) {
        addInvestmentInternal(investment.copy(
            date = System.currentTimeMillis()
        ))
    }

    @Delete
    suspend fun removeInvestment(investment: Investment)

    @Query("SELECT * FROM investments")
    suspend fun getInvestments(): List<Investment>

    @Query("SELECT * FROM investments")
    fun getInvestmentsFlow(): Flow<List<Investment>>
}