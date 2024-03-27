package com.team.app.data.repositories

import com.team.app.data.database.InvestmentsDao
import com.team.app.data.database.model.Investment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.team.app.data.model.Investment as InvestmentModel

class InvestmentsRepository(
    private val investmentsDao: InvestmentsDao
) {
    private fun getInvestmentsFlow(): Flow<List<InvestmentModel>> {
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
                        leverage = investment.leverage,
                        date = investment.date
                    )
                }
            }
    }

    fun getInvestmentsFlow(symbol: String): Flow<List<InvestmentModel>> {
        return investmentsDao
            .getInvestmentsFlow()
            .map { investments ->
                investments
                    .filter {
                        it.symbol == symbol
                    }
                    .map { investment ->
                    InvestmentModel(
                        id = investment.id,
                        symbol = investment.symbol,
                        type = investment.type,
                        amount = investment.amount,
                        price = investment.price,
                        leverage = investment.leverage,
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
                leverage = investment.leverage,
                date = investment.date
            )
        }
    }

    fun getBalanceFlow(symbol: String, currentPrice: Double): Flow<Double> {
        return getInvestmentsFlow()
            .map { investments ->
                investments
                    .filter { it.symbol == symbol }
                    .sumOf { it.amount * (currentPrice / it.price) }
            }
    }

    suspend fun addInvestment(investment: InvestmentModel) {
        investmentsDao.addInvestment(
            Investment(
                symbol = investment.symbol,
                type = investment.type,
                amount = investment.amount,
                price = investment.price,
                leverage = investment.leverage,
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
                leverage = investment.leverage,
                date = investment.date
            )
        )
    }

}