package com.team.app.data.model

data class StepCountData(
    val steps: Long,
    val timestamp: Long = System.currentTimeMillis(),
)
