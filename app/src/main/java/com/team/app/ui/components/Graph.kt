package com.team.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
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