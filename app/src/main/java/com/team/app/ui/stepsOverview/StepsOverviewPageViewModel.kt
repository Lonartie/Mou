package com.team.app.ui.stepsOverview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.lineSeries
import com.team.app.data.model.StepCountData
import com.team.app.data.repositories.StepCounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class StepsOverviewPageViewModel @Inject constructor(
    private val stepCounterRepo: StepCounterRepository
) : ViewModel() {

    val modelProducer = CartesianChartModelProducer.build()
    val xAxisKey: ExtraStore.Key<List<String>> = ExtraStore.Key()
    val minMaxKey: ExtraStore.Key<Pair<Float, Float>> = ExtraStore.Key()
    val currentCategory = mutableStateOf("Day")
    val dataPresent = mutableStateOf(false)

    suspend fun init() {
        updateGraph()
    }

    suspend fun onCategoryChanged(category: String) {
        currentCategory.value = category

        updateGraph()
    }

    private suspend fun updateGraph() {
        val hour = 60L * 60 * 1000
        val day = 24L * 60 * 60 * 1000

        val groupBy = when (currentCategory.value) {
            "Day" -> hour
            "Week" -> day
            "Month" -> day
            else -> 0
        }

        val valueCount = when(currentCategory.value) {
            "Day" -> 24
            "Week" -> 7
            "Month" -> 30
            else -> 0
        }

        val current = System.currentTimeMillis()
        val beginTime = current - valueCount * groupBy
        val xPresets = (current - valueCount * groupBy)..current step groupBy

        val additiveData = stepCounterRepo.getStepCountDataModelSince(beginTime)
        val data = mutableListOf<StepCountData>()

        for (i in 1 until additiveData.size) {
            val timestamp = additiveData[i].timestamp
            val steps = max(0, additiveData[i].steps - additiveData[i - 1].steps)
            data.add(StepCountData(timestamp, steps))
        }

        dataPresent.value = data.isNotEmpty()

        if (data.isEmpty()) {
            modelProducer.tryRunTransaction {
                lineSeries { series(listOf(0, 0)) }
                updateExtras {
                    it[xAxisKey] = listOf("-", "-")
                    it[minMaxKey] = 0f to 1f
                }
            }
            return
        }

        val yData = xPresets.map { preset ->
            data.filter {
                it.timestamp in preset - groupBy until preset
            }.sumOf {
                it.steps.toInt()
            }
        }

        val xData = xPresets.map {
            when (currentCategory.value) {
                "Day" -> LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("H", Locale.US))

                "Week" -> LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("E", Locale.US))

                "Month" -> LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("d", Locale.US))

                else -> ""
            }
        }

        println("xData: $xData")
        println("yData: $yData")

        val minY = yData.minOrNull() ?: 0
        var maxY = yData.maxOrNull() ?: 0
        if (maxY - minY < 1) {
            maxY = minY + 1
        }
        modelProducer.tryRunTransaction {
            lineSeries { series(yData) }
            updateExtras {
                it[xAxisKey] = xData
                it[minMaxKey] = minY.toFloat() to maxY.toFloat()
            }
        }
    }
}