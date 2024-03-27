package com.team.app.ui.investment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.team.app.R
import com.team.app.data.model.Investment
import com.team.app.data.repositories.StocksRepository
import com.team.app.ui.common.Graph
import com.team.app.ui.common.TopAppBar
import com.team.app.utils.earningsFromSell
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
@Preview
fun InvestmentPage(
    goBack: () -> Unit = {},
    symbol: String = "AAPL",
    viewModel: InvestmentPageViewModel = hiltViewModel()
) {
    val currentCategory = viewModel.currentCategory.value
    val modelProducer = viewModel.modelProducer
    val xAxisKey = viewModel.xAxisKey
    val minMaxKey = viewModel.minMaxKey
    val networkStatus = viewModel.networkStatus.collectAsState(false).value
    val coro = rememberCoroutineScope()
    val balance = viewModel.balance.collectAsState(0.0).value.toDouble()
    val investmentsValue = viewModel.investmentsValue?.collectAsState(0.0)?.value ?: 0.0
    val investments = viewModel.investments?.collectAsState(emptyList())?.value ?: emptyList()
    val price = viewModel.currentPrice.doubleValue
    val doneLoading = viewModel.doneLoading.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.init(symbol)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.investment_formatted, symbol),
                onBackClick = goBack
            )
        }
    ) { contentPadding ->
        if (networkStatus) {
            Content(
                currentCategory = currentCategory,
                onCategoryChanged = { coro.launch { viewModel.changeCategory(it) } },
                modelProducer = modelProducer,
                contentPadding = contentPadding,
                xAxisKey = xAxisKey,
                minMaxKey = minMaxKey,
                onBuy = { coins, leverage ->
                    coro.launch {
                        val msg = viewModel.buy(coins, leverage)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                },
                balance = balance,
                investmentsValue = investmentsValue,
                investments = investments,
                currentPrice = price,
                onSell = {
                    coro.launch {
                        val msg = viewModel.onSell(it)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                },
                doneLoading = doneLoading
            )
        } else {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.no_connection)
            )
        }
    }
}

@Composable
@Preview
fun Content(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    currentCategory: StocksRepository.TimeSeriesCategory =
        StocksRepository.TimeSeriesCategory.YEAR,
    onCategoryChanged: (StocksRepository.TimeSeriesCategory) -> Unit = {},
    modelProducer: CartesianChartModelProducer = CartesianChartModelProducer.build(),
    xAxisKey: ExtraStore.Key<List<String>> = ExtraStore.Key(),
    minMaxKey: ExtraStore.Key<Pair<Float, Float>> = ExtraStore.Key(),
    onBuy: (String, String) -> Unit = { _, _ -> },
    onSell: (Investment) -> Unit = {},
    balance: Double = 0.0,
    investmentsValue: Double = 0.0,
    investments: List<Investment> = listOf(
        Investment(
            id = 0,
            symbol = "AAPL",
            type = "stock",
            amount = 1.0,
            price = 100.0,
            leverage = 1.0,
            date = 0
        )
    ),
    currentPrice: Double = 0.0,
    doneLoading: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!doneLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Graph(modelProducer, xAxisKey, minMaxKey)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (category in StocksRepository.TimeSeriesCategory.entries.reversed()) {
                    Button(
                        onClick = { onCategoryChanged(category) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                            if (category == currentCategory)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        val categoryName = category.name.lowercase(Locale.getDefault())
                            .replaceFirstChar { it.uppercase(Locale.getDefault()) }
                        Text(text = categoryName)
                    }
                }
            }

            Divider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = stringResource(id = R.string.invest),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = stringResource(R.string.balance_formatted, "%.2f".format(balance)))
                    Text(
                        text = stringResource(
                            R.string.investments_formatted, "%.2f".format(investmentsValue)
                        )
                    )
                }

                val coins = remember { mutableStateOf("0") }
                TextField(
                    label = { Text(text = stringResource(id = R.string.coins)) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    value = coins.value,
                    onValueChange = {
                        if (it.toIntOrNull() != null || it.isEmpty()) {
                            coins.value = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                )

                val leverage = remember { mutableStateOf("1") }
                TextField(
                    label = { Text(text = stringResource(id = R.string.leverage)) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    value = leverage.value,
                    onValueChange = {
                        if (it.toIntOrNull() != null || it.isEmpty()) {
                            leverage.value = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    Button(onClick = {
                        onBuy(coins.value, leverage.value)
                        coins.value = 0.toString()
                        leverage.value = 1.toString()
                    }) {
                        Text(text = stringResource(id = R.string.invest))
                    }
                }

                for (investment in investments) {
                    InvestmentCard(
                        price = currentPrice,
                        investment = investment,
                        onSell = onSell
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun InvestmentCard(
    price: Double = 0.0,
    investment: Investment = Investment(
        0, "AAPL", "Stock", 10.0,
        100.0, 1.0, System.currentTimeMillis()
    ),
    onSell: (Investment) -> Unit = {}
) {
    val changedInPercent = price / investment.price
    val currentAmount = investment.amount * investment.leverage * changedInPercent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = investment.symbol)
            Text(text = investment.amount.toInt().toString())
            Text(text = "%.2f".format(currentAmount))
            Text(
                text = "%.2f%%".format((changedInPercent - 1) * 100),
                color = if ((changedInPercent - 1) >= 0) Color.Green
                else Color.Red
            )
            Text(text = investment.earningsFromSell(price).toString())
            Button(
                onClick = { onSell(investment) }
            ) {
                Text(text = stringResource(id = R.string.sell))
            }
        }
    }
}