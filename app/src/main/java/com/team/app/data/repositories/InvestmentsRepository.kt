package com.team.app.data.repositories

import com.team.app.data.database.InvestmentsDao
import com.team.app.data.database.model.Investment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.team.app.data.model.Investment as InvestmentModel

class InvestmentsRepository(
    private val investmentsDao: InvestmentsDao,
    private val stocksRepo: StocksRepository
) {
    fun getInvestmentsFlow(): Flow<List<InvestmentModel>> {
        return investmentsDao
            .getInvestmentsFlow()
            .map { investments ->
                investments.map { investment ->
                    InvestmentModel(
                        id = investment.id,
                        symbol = investment.symbol,
                        type = investment.type,
                        amount = investment.amount,
                        price = investment.price,
                        date = investment.date
                    )
                }
            }
    }

    suspend fun getInvestments(): List<InvestmentModel> {
        return investmentsDao.getInvestments().map { investment ->
            InvestmentModel(
                id = investment.id,
                symbol = investment.symbol,
                type = investment.type,
                amount = investment.amount,
                price = investment.price,
                date = investment.date
            )
        }
    }

    suspend fun addInvestment(investment: InvestmentModel) {
        investmentsDao.addInvestment(
            Investment(
                symbol = investment.symbol,
                type = investment.type,
                amount = investment.amount,
                price = investment.price,
                date = investment.date
            )
        )
    }

    suspend fun removeInvestment(investment: InvestmentModel) {
        investmentsDao.removeInvestment(
            Investment(
                id = investment.id,
                symbol = investment.symbol,
                type = investment.type,
                amount = investment.amount,
                price = investment.price,
                date = investment.date
            )
        )
    }

}