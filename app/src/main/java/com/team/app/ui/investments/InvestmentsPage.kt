package com.team.app.ui.investments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.app.data.model.CombinedInvestments
import com.team.app.data.model.Symbol
import com.team.app.ui.common.TopAppBar
import kotlinx.coroutines.launch

@Composable
@Preview
fun InvestmentsPage(
    goBack: () -> Unit = {},
    openInvestment: (String) -> Unit = {},
    viewModel: InvestmentsPageViewModel = hiltViewModel()
) {
    val myInvestments = viewModel.myInvestments.value
    val searchedStocks = viewModel.searchedStocks.value
    val networkStatus = viewModel.networkStatus.collectAsState(false).value
    val prices = viewModel.prices.value
    val doneLoading = viewModel.doneLoading.value
    val coro = rememberCoroutineScope()

    remember {
        coro.launch {
            viewModel.init()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar("Investments", onBackClick = goBack) }) { contentPadding ->
        if (networkStatus) {
            Content(
                investments = myInvestments,
                prices = prices,
                onSearchTextChange = viewModel::searchStock,
                searchedStocks = searchedStocks,
                openInvestment = openInvestment,
                contentPadding = contentPadding,
                doneLoading = doneLoading,
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                textAlign = TextAlign.Center,
                text = "No network connection"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Content(
    investments: List<CombinedInvestments> = listOf(
        CombinedInvestments(
            "AAPL", 100,
            115, 0.15
        )
    ),
    prices: List<Double> = listOf(1.0),
    onSearchTextChange: suspend (String) -> Unit = {},
    searchedStocks: List<Symbol> = listOf(
        Symbol("AAPL", "Apple Inc.", "Germany", "Common Stock", "USD")
    ),
    openInvestment: (String) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
    doneLoading: Boolean = false
) {
    val coro = rememberCoroutineScope()
    val queryText = remember { mutableStateOf("") }
    val active = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Search bar
        SearchBar(
            query = queryText.value,
            onQueryChange = { queryText.value = it },
            onSearch = { text -> coro.launch { onSearchTextChange(text) } },
            active = active.value,
            onActiveChange = { active.value = it },
            placeholder = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            LazyColumn {
                items(searchedStocks) { stock ->
                    StockCard(
                        openInvestment = openInvestment, stock = stock
                    )
                }
            }
        }

        if (!doneLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            for (i in investments.indices) {
                val investment = investments[i]
                val price = prices.getOrNull(i) ?: 0.0
                InvestmentCard(
                    openInvestment = openInvestment, currentPrice = price, investment = investment
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InvestmentCard(
    currentPrice: Double = 1.0,
    investment: CombinedInvestments =
        CombinedInvestments(
            "AAPL", 100,
            115, 0.15
        ),
    openInvestment: (String) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp),
        onClick = { openInvestment(investment.symbol) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = investment.symbol)
            Text(text = investment.investedAmount.toString())
            Text(
                text = "%.2f%%".format(investment.percentage * 100),
                color = if (investment.percentage >= 0) Color.Green
                else Color.Red
            )
            Text(text = investment.earnings.toString())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun StockCard(
    stock: Symbol = Symbol(
        "AAPL",
        "Apple Inc.",
        "Germany",
        "Common Stock",
        "USD"
    ),
    openInvestment: (String) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp),
        onClick = { openInvestment(stock.symbol) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stock.symbol)
                Text(text = stock.country)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stock.instrumentName)
                Text(text = stock.instrumentType)
            }
        }
    }
}