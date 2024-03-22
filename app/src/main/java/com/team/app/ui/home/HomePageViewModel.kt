package com.team.app.ui.home

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.lonartie.bookdiary.data.repositories.SettingsRepository
import com.team.app.R
import com.team.app.data.model.Attributes
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val settings: SettingsRepository
) : ViewModel() {

    val firstStart = settings.firstStart
    val attributes = settings.attributes
    val currentFood = settings.currentFood
    val currentToy = settings.currentToy
    val currentMisc = settings.currentMisc
    val figureState = mutableIntStateOf(R.drawable.figure_happy)
	
	private fun getMinimalAttributeValue(attributes: Attributes): Int {
        return min(
            attributes.hunger,
            min(
                attributes.happiness,
                attributes.health
            )
        )

    }
    fun setFigureState(attributes: Attributes) {
        val minimalValue = getMinimalAttributeValue(attributes)
        figureState.intValue = when {
            minimalValue >= 75 -> R.drawable.figure_happy
            minimalValue >= 50 -> R.drawable.figure_lonely
            else -> R.drawable.figure_angry
        }
    }

    suspend fun onStart(isFirst : Boolean) {
        var item = Item(ItemType.FOOD, "Chicken", 10, 10)
        println("current food: $item")
        settings.saveCurrentItem(item)
        item = Item(ItemType.TOY, "Ball", 5, 5)
        println("current toy: $item")
        settings.saveCurrentItem(item)
        item = Item(ItemType.MEDICINE, "Medicine", 15, 15)
        println("current misc: $item")
        settings.saveCurrentItem(item)
    }

    //suspend fun openMoneyScreen() {
    //    println("Open money screen")
    //}

    //suspend fun openShop() {
    //    println("Open shop")
    //}

    suspend fun giveFood(item: Item, attributes: Attributes) {
        val newAttributes = attributes.copy(hunger = attributes.hunger + item.actionValue)
        settings.saveAttributes(newAttributes)
        setFigureState(attributes)
        println("hunger before: ${attributes.hunger}, after: ${newAttributes.hunger}")
    }

    suspend fun selectFood() {
        println("Select food")
    }

    suspend fun giveToy(item: Item, attributes: Attributes) {
        val newAttributes = attributes.copy(happiness = attributes.happiness + item.actionValue)
        settings.saveAttributes(newAttributes)
        setFigureState(attributes)
        println("happiness before: ${attributes.happiness}, after: ${newAttributes.happiness}")
    }

    suspend fun selectToy() {
        println("Select toy")
    }

    suspend fun giveItem(item: Item, attributes: Attributes) {
        if (item.itemType == ItemType.MEDICINE) {
            val newAttributes = attributes.copy(health = attributes.health + item.actionValue)
            settings.saveAttributes(newAttributes)
            setFigureState(attributes)
            println("health before: ${attributes.health}, after: ${newAttributes.health}")
        }
    }

    suspend fun selectItem() {
        println("Select item")
    }
}