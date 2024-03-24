package com.team.app.ui.investment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import com.team.app.data.repositories.StocksRepository
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
    val points = viewModel.currentTimeSeries.value
    val currentCategory = viewModel.currentCategory.value
    val coro = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.init(symbol)
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = title, onBackClick = goBack) }) { contentPadding ->
        Content(
            points = points,
            currentCategory = currentCategory,
            indexMapper = viewModel::indexMapper,
            onCategoryChanged = { coro.launch { viewModel.changeCategory(it) } },
            contentPadding = contentPadding
        )
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
    points: List<DataPoint> = listOf(DataPoint(0.0F, 0.0F), DataPoint(10.0F, 10.0F)),
    currentCategory: StocksRepository.TimeSeriesCategory = StocksRepository.TimeSeriesCategory.YEAR,
    onCategoryChanged: (StocksRepository.TimeSeriesCategory) -> Unit = {},
    indexMapper: (Float) -> String = { it.toString() }
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
            if (points.isNotEmpty()) {
                LineGraph(
                    plot = LinePlot(
                        listOf(
                            LinePlot.Line(
                                points,
                                LinePlot.Connection(color = Color.Red),
                                LinePlot.Intersection(color = Color.Red),
                                LinePlot.Highlight(color = Color.Yellow),
                            )
                        ),
                        grid = LinePlot.Grid(Color.Red),
                        xAxis = LinePlot.XAxis(
                            steps = 10,
                            content = { min, offset, max ->
                                for (it in 0 until 10) {
                                    val value = it * offset + min
                                    val text = indexMapper(value)
                                    Text(
                                        text = text,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = androidx.compose.material.MaterialTheme.typography.caption,
                                        color = androidx.compose.material.MaterialTheme.colors.onSurface
                                    )
                                    if (value > max) {
                                        break
                                    }
                                }
                            }
                        )
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

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