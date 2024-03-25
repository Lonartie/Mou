package com.team.app.ui.investments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.team.app.data.model.Symbol
import com.team.app.data.repositories.InvestmentsRepository
import com.team.app.data.repositories.NetworkRepository
import com.team.app.data.repositories.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestmentsPageViewModel @Inject constructor(
    private val stocksRepo: StocksRepository,
    private val investmentsRepo: InvestmentsRepository,
    private val networkRepo: NetworkRepository
) : ViewModel() {

    val myInvestments = investmentsRepo.getInvestmentsFlow()
    val searchText = mutableStateOf("")
    val searchedStocks = mutableStateOf(emptyList<Symbol>())
    val networkStatus = networkRepo.networkStatus

    suspend fun getPrice(symbol: String): Double {
        return stocksRepo.getPrice(symbol)
    }

    suspend fun searchStock(query: String) {
        searchText.value = query
        if (query.isEmpty()) {
            searchedStocks.value = emptyList()
            return
        }

        searchedStocks.value = stocksRepo.searchSymbol(query)
    }

    suspend fun updateSearch(query: String) {
        searchText.value = query
    }
}