package com.team.app.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.team.app.data.model.Attributes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {
    val attributes = mutableStateOf(Attributes(20, 90, 75, 14))

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