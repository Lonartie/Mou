package com.team.app.ui.investment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.team.app.data.repositories.StocksRepository
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

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

    LaunchedEffect(Unit) {
        viewModel.init(symbol)
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = title, onBackClick = goBack) }) { contentPadding ->
        if (networkStatus) {
            Content(
                currentCategory = currentCategory,
                onCategoryChanged = { coro.launch { viewModel.changeCategory(it) } },
                modelProducer = modelProducer,
                contentPadding = contentPadding,
                xAxisKey = xAxisKey,
                minMaxKey = minMaxKey
            )
        } else {
            Text(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
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
    currentCategory: StocksRepository.TimeSeriesCategory = StocksRepository.TimeSeriesCategory.YEAR,
    onCategoryChanged: (StocksRepository.TimeSeriesCategory) -> Unit = {},
    modelProducer: CartesianChartModelProducer = CartesianChartModelProducer.build(),
    xAxisKey: ExtraStore.Key<List<String>> = ExtraStore.Key(),
    minMaxKey: ExtraStore.Key<Pair<Float, Float>> = ExtraStore.Key(),
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                            containerColor = if (category == currentCategory) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        val categoryName = category.name.lowercase(Locale.getDefault())
                            .replaceFirstChar { it.uppercase(Locale.getDefault()) }
                        Text(text = categoryName)
                    }
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