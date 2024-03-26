package com.team.app.ui.investments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.team.app.data.model.CombinedInvestments
import com.team.app.data.model.Symbol
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.NetworkRepository
import com.team.app.data.repositories.StocksRepository
import com.team.app.utils.earningsFromSell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class InvestmentsPageViewModel @Inject constructor(
    private val stocksRepo: StocksRepository,
    private val investmentsRepo: InvestmentsRepository,
    networkRepo: NetworkRepository
) : ViewModel() {

    val myInvestments = mutableStateOf(emptyList<CombinedInvestments>())
    val searchedStocks = mutableStateOf(emptyList<Symbol>())
    val networkStatus = networkRepo.networkStatus
    val prices = mutableStateOf(emptyList<Double>())
    val doneLoading = mutableStateOf(false)

    suspend fun init() {
        doneLoading.value = false
        myInvestments.value = emptyList()
        val investments = investmentsRepo.getInvestments()

        // group investments by symbol
        val mappedInvestments = investments.groupBy { it.symbol }

        // reduce investments to a single CombinedInvestments object
        var index = 0
        mappedInvestments.forEach { (symbol, investments) ->
            val currentPrice = stocksRepo.getPrice(symbol)
            var combined = CombinedInvestments(
                symbol = investments[0].symbol,
                investedAmount = investments[0].amount.toInt(),
                earnings = investments[0].earningsFromSell(currentPrice),
                percentage = currentPrice / investments[0].price - 1
            )

            for (i in 1 until investments.size) {
                val investment = investments[i]
                combined = combined.copy(
                    investedAmount = combined.investedAmount + investment.amount.toInt(),
                    earnings = combined.earnings + investment.earningsFromSell(currentPrice)
                )
            }

            myInvestments.value = myInvestments.value + combined
            if (index++ < mappedInvestments.size - 1) {
                delay(2000)
            }
        }
        doneLoading.value = true
    }

    suspend fun searchStock(query: String) {
        if (query.isEmpty()) {
            searchedStocks.value = emptyList()
            return
        }

        searchedStocks.value = stocksRepo.searchSymbol(query)
            .distinctBy { listOf(it.symbol, it.instrumentName, it.instrumentType) }
    }
}