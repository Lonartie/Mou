package com.team.app.ui.home

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.team.app.R
import com.team.app.data.model.Attributes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {
    val attributes = mutableStateOf(Attributes(20, 90, 90, 40))
    val figureState = mutableIntStateOf(R.drawable.figure_happy)

    private fun getMinimalAttributeValue(): Int {
        return min(
            attributes.value.hunger,
            min(
                attributes.value.happiness,
                attributes.value.health
            )
        )

    }
    fun setFigureState() {
        val minimalValue = getMinimalAttributeValue()
        figureState.intValue = when {
            minimalValue >= 75 -> R.drawable.figure_happy
            minimalValue >= 50 -> R.drawable.figure_lonely
            else -> R.drawable.figure_angry
        }
    }

    fun openMoneyScreen() {
        println("Open money screen")
    }

    fun openShop() {
        println("Open shop")
    }

    fun giveFood() {
        println("Give food")
    }

    fun selectFood() {
        println("Select food")
    }

    fun giveToy() {
        println("Give toy")
    }

    fun selectToy() {
        println("Select toy")
    }

    fun giveItem() {
        println("Give item")
    }

    fun selectItem() {
        println("Select item")
    }
}