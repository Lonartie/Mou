package com.team.app.ui.home

import androidx.annotation.RawRes
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.R
import com.team.app.data.model.Attributes
import com.team.app.data.model.Hotbar
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.SettingsRepository
import com.team.app.data.repositories.StepCounterRepository
import com.team.app.utils.SoundManager
import com.team.app.utils.Constants
import com.team.app.utils.Constants.Companion.INVALID_INVENTORY_ITEM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val stepCounter: StepCounterRepository,
    private val attributesRepo: AttributesRepository,
    private val inventoryRepo: InventoryRepository,
    private val hotbarRepo: HotbarRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    val figureState = mutableIntStateOf(R.drawable.figure_happy)
    val attributes: Flow<Attributes> = attributesRepo.getAttributesFlow()
    val earnedCoins = mutableIntStateOf(0)

    val hotbar = mutableStateOf(
        Hotbar(
            INVALID_INVENTORY_ITEM,
            INVALID_INVENTORY_ITEM,
            INVALID_INVENTORY_ITEM
        )
    )

    private suspend fun updateHotbar() {
        hotbar.value = hotbarRepo.getHotbar()
        println("Hotbar updated")
    }

    suspend fun onStart() {
        updateHotbar()
        setFigureState(attributesRepo.getAttributes())
        changeCoins()
    }

    suspend fun giveFood(item: Item, attributes: Attributes) {
        if (item.name == "") return
        if (attributes.hunger == Constants.MAX_HUNGER) return

        viewModelScope.launch {
            playSound(R.raw.use_food)
        }

        attributesRepo.updateHunger(attributes.hunger + item.actionValue)
        // avoid more than 100% hunger
        if (attributesRepo.getAttributes().hunger > Constants.MAX_HUNGER) {
            attributesRepo.updateHunger(Constants.MAX_HUNGER)
        }
        giveGeneric(item, attributes)
    }

    suspend fun giveToy(item: Item, attributes: Attributes) {
        if (item.name == "") return
        if (attributes.happiness == Constants.MAX_HAPPINESS) return

        viewModelScope.launch {
            playSound(R.raw.use_toy)
        }

        attributesRepo.updateHappiness(attributes.happiness + item.actionValue)
        // avoid more than 100% happiness
        if (attributesRepo.getAttributes().happiness > Constants.MAX_HAPPINESS) {
            attributesRepo.updateHappiness(Constants.MAX_HAPPINESS)
        }
        giveGeneric(item, attributes)

    }

    suspend fun giveItem(item: Item, attributes: Attributes) {
        if (item.name == "") return

        if (item.itemType == ItemType.MEDICINE) {
            if (attributes.health == Constants.MAX_HEALTH) return

            viewModelScope.launch {
                playSound(R.raw.use_item)
            }

            attributesRepo.updateHealth(attributes.health + item.actionValue)
            // avoid more than 100% health
            if (attributesRepo.getAttributes().health > Constants.MAX_HEALTH) {
                attributesRepo.updateHealth(Constants.MAX_HEALTH)
            }
            giveGeneric(item, attributes)
        }
    }

    private suspend fun giveGeneric(item: Item, attributes: Attributes) {
        if (item.name == "") return

        val items = inventoryRepo.getItems()
        if (items.any { it.item.name == item.name }) {
            val invItem = items.first { it.item.name == item.name }
            if (invItem.quantity == 1) {
                println("Removing item from hotbar ${item.name} ${item.itemType}")
                when (item.itemType) {
                    ItemType.FOOD -> hotbarRepo.setFood(0)
                    ItemType.TOY -> hotbarRepo.setToy(0)
                    ItemType.MISC -> hotbarRepo.setMisc(0)
                    ItemType.MEDICINE -> hotbarRepo.setMisc(0)
                }
                println("Updated hotbar: ${hotbarRepo.getHotbar()}")
            }
        }

        inventoryRepo.removeOne(item)
        updateHotbar()
        setFigureState(attributes)
    }

    fun playSound(@RawRes res: Int) = soundManager.play(res)

    private suspend fun changeCoins() {
        stepCounter.insertFirstStartTimestamp()

        val steps = stepCounter.getStepsSinceStart()
        attributesRepo.updateCoins(attributesRepo.getAttributes().coins + steps.toInt())
        stepCounter.insertStartTimestamp()
        stepCounter.insertStepCount()
        earnedCoins.intValue = steps.toInt()
    }

    private fun getMinimalAttributeValue(attributes: Attributes): Int {
        return min(
            attributes.hunger,
            min(
                attributes.happiness,
                attributes.health
            )
        )

    }

    private suspend fun resetAttributes() {
        attributesRepo.updateHunger(Constants.MAX_HUNGER)
        attributesRepo.updateHappiness(Constants.MAX_HAPPINESS)
        attributesRepo.updateHealth(Constants.MAX_HEALTH)
        setFigureState(attributesRepo.getAttributes())
    }

    suspend fun isDead() = attributesRepo.isDead()

    private suspend fun increaseDieCount() {
        settings.incDieCount()
    }

    suspend fun handleDied() {
        increaseDieCount()
        resetAttributes()
    }

    suspend fun getDieCountNow() = settings.getDieCount()

    private fun setFigureState(attributes: Attributes) {
        val minimalValue = getMinimalAttributeValue(attributes)
        figureState.intValue = when {
            minimalValue >= 75 -> R.drawable.figure_happy
            minimalValue >= 50 -> R.drawable.figure_lonely
            else -> R.drawable.figure_angry
        }
    }
}