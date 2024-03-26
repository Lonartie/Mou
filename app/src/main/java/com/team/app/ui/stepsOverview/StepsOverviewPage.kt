package com.team.app.ui.stepsOverview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.team.app.ui.components.Graph
import kotlinx.coroutines.launch

@Composable
@Preview
fun StepsOverviewPage(
    goBack: () -> Unit = {},
    viewModel: StepsOverviewPageViewModel = hiltViewModel()
) {
    val modelProducer = viewModel.modelProducer
    val xAxisKey = viewModel.xAxisKey
    val minMaxKey = viewModel.minMaxKey
    val coro = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Steps overview",
                onBackClick = goBack
            )
        }
    ) { contentPadding ->
        Content(
            padding = contentPadding,
            modelProducer = modelProducer,
            xAxisKey = xAxisKey,
            minMaxKey = minMaxKey,
            onCategoryChanged = { coro.launch { viewModel.onCategoryChanged(it) } },
            currentCategory = viewModel.currentCategory.value,
            dataPresent = viewModel.dataPresent.value
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String = "Steps overview",
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
    padding: PaddingValues = PaddingValues(0.dp),
    modelProducer: CartesianChartModelProducer = CartesianChartModelProducer.build(),
    xAxisKey: ExtraStore.Key<List<String>> = ExtraStore.Key(),
    minMaxKey: ExtraStore.Key<Pair<Float, Float>> = ExtraStore.Key(),
    categories: List<String> = listOf("Day", "Week", "Month"),
    currentCategory: String = "Day",
    onCategoryChanged: (String) -> Unit = {},
    dataPresent: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (!dataPresent) {
            Text(
                text = "No data available",
                fontSize = 20.sp,
            )
        }

        Graph(
            modelProducer = modelProducer,
            xAxisKey = xAxisKey,
            minMaxKey = minMaxKey
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (category in categories.reversed()) {
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
                    Text(text = category)
                }
            }
        }
    }
}