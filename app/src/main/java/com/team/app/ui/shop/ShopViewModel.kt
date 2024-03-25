package com.team.app.ui.shop

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.R
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.AttributesRepository
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
import com.team.app.service.SoundService
import com.team.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val inventoryRepo: InventoryRepository,
    private val hotbarRepo: HotbarRepository,
    private val soundService: SoundService,
    private val attributesRepo: AttributesRepository,
    itemsRepo: ItemsRepository
) : ViewModel() {
    var itemsFlow = itemsRepo.getItemsNoInvalidFlow()
        private set

    var attributesFlow = attributesRepo.getAttributesFlow()
        private set

    fun buyItem(item: Item) {
        viewModelScope.launch {
            val userCoins = attributesRepo.getAttributes().coins

            // check if user has enough money
            if (userCoins >= item.price) {
                viewModelScope.launch {
                    playSound(R.raw.purchase_item)
                }
            }

            inventoryRepo.addOne(item)

            // keep hotbar up-to-date
            val hotbar = hotbarRepo.getHotbar()

            val foodItem = hotbar.foodItem
            val toyItem = hotbar.toyItem
            val miscItem = hotbar.miscItem

            val inventoryItems = inventoryRepo.getItems()
            val inventoryContainsType = { type: ItemType ->
                inventoryItems.any { it.item.name != "" && it.item.itemType == type }
            }
            val firstItemOfType = { type: ItemType ->
                inventoryItems.first { it.item.name != "" && it.item.itemType == type }
            }

            if (foodItem == Constants.INVALID_INVENTORY_ITEM && inventoryContainsType(ItemType.FOOD)) {
                val id = inventoryRepo.getIdOfItem(firstItemOfType(ItemType.FOOD))!!
                println("Setting food to $id")
                hotbarRepo.setFood(id)
            } else if (toyItem == Constants.INVALID_INVENTORY_ITEM && inventoryContainsType(ItemType.TOY)) {
                val id = inventoryRepo.getIdOfItem(firstItemOfType(ItemType.TOY))!!
                println("Setting toy to $id")
                hotbarRepo.setToy(id)
            } else if (miscItem == Constants.INVALID_INVENTORY_ITEM && inventoryContainsType(ItemType.MEDICINE)) {
                val id = inventoryRepo.getIdOfItem(firstItemOfType(ItemType.MEDICINE))!!
                println("Setting misc to $id")
                hotbarRepo.setMisc(id)
            } else if (miscItem == Constants.INVALID_INVENTORY_ITEM && inventoryContainsType(ItemType.MISC)) {
                val id = inventoryRepo.getIdOfItem(firstItemOfType(ItemType.MISC))!!
                println("Setting misc to $id")
                hotbarRepo.setMisc(id)
            }
        }
    }

    private fun playSound(@RawRes res: Int) = soundService.play(res)
}