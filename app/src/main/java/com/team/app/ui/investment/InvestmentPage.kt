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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValueOverrider
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.sample.showcase.rememberMarker
import com.team.app.data.model.Investment
import com.team.app.data.repositories.StocksRepository
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
    val title = "Investment: $symbol"
    val currentCategory = viewModel.currentCategory.value
    val modelProducer = viewModel.modelProducer
    val xAxisKey = viewModel.xAxisKey
    val minMaxKey = viewModel.minMaxKey
    val networkStatus = viewModel.networkStatus.collectAsState(false).value
    val coro = rememberCoroutineScope()
    val coins = viewModel.coins.intValue
    val leverage = viewModel.leverage.intValue
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
        topBar = { TopAppBar(title = title, onBackClick = goBack) }) { contentPadding ->
        if (networkStatus) {
            Content(
                currentCategory = currentCategory,
                onCategoryChanged = { coro.launch { viewModel.changeCategory(it) } },
                modelProducer = modelProducer,
                contentPadding = contentPadding,
                xAxisKey = xAxisKey,
                minMaxKey = minMaxKey,
                coins = coins,
                leverage = leverage,
                onCoinsChange = viewModel::changeCoins,
                onLeverageChange = viewModel::changeLeverage,
                onBuy = {
                    coro.launch {
                        val msg = viewModel.buy()
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
                text = "No network connection"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = title, color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), modifier = modifier, navigationIcon = {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, contentDescription = null
            )
        }
    })
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
    coins: Int = 0,
    leverage: Int = 1,
    onCoinsChange: (String) -> Unit = {},
    onLeverageChange: (String) -> Unit = {},
    onBuy: () -> Unit = {},
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
                    text = "Invest",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Balance: ${"%.2f".format(balance)}")
                    Text("Investments: ${"%.2f".format(investmentsValue)}")
                }

                TextField(
                    label = { Text("Coins") },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    value = coins.toString(),
                    onValueChange = onCoinsChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                TextField(
                    label = { Text("Leverage") },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    ),
                    value = leverage.toString(),
                    onValueChange = onLeverageChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    Button(onClick = onBuy) {
                        Text("Invest")
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

@Composable
@Preview
fun Graph(
    modelProducer: CartesianChartModelProducer = CartesianChartModelProducer.build(),
    xAxisKey: ExtraStore.Key<List<String>> = ExtraStore.Key(),
    minMaxKey: ExtraStore.Key<Pair<Float, Float>> = ExtraStore.Key(),
) {

    val minMaxOverrider = object : AxisValueOverrider {
        override fun getMinY(minX: Float, maxX: Float, extraStore: ExtraStore): Float {
            return AxisValueOverrider.fixed(
                minY = extraStore[minMaxKey].first,
                maxY = extraStore[minMaxKey].second
            ).getMinY(minX, maxX, extraStore)
        }

        override fun getMaxY(minY: Float, maxY: Float, extraStore: ExtraStore): Float {
            return AxisValueOverrider.fixed(
                minY = extraStore[minMaxKey].first,
                maxY = extraStore[minMaxKey].second
            ).getMaxY(minY, maxY, extraStore)
        }

    }

    val lineLayer = rememberLineCartesianLayer(
        axisValueOverrider = minMaxOverrider
    )
    val chart = rememberCartesianChart(
        lineLayer,
        startAxis = rememberStartAxis(
            itemPlacer = remember {
                AxisItemPlacer.Vertical.count(count = { 4 })
            }
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { x, chartValues, _ ->
                chartValues.model.extraStore[xAxisKey][x.toInt()]
            },
        )
    )
    val marker = rememberMarker()

    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        marker = marker,
    )
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
                Text("Sell")
            }
        }
    }
}