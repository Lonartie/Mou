package com.team.app.ui.inventory

import androidx.lifecycle.ViewModel
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val invRepo: InventoryRepository,
    private val hotbarRepo: HotbarRepository
) : ViewModel() {
    fun getItemsWithTypes(types: List<ItemType>) = runBlocking {
        invRepo.getItems().filter {
            types.contains(it.item.itemType) && it.item.name != ""
        }.sortedBy {
            it.item.name
        }
    }

    suspend fun setHotbarItem(invItem: InventoryItem) {
        when (invItem.item.itemType) {
            ItemType.FOOD -> hotbarRepo.setFood(invRepo.getIdOfItem(invItem)!!)
            ItemType.TOY -> hotbarRepo.setToy(invRepo.getIdOfItem(invItem)!!)
            ItemType.MISC -> hotbarRepo.setMisc(invRepo.getIdOfItem(invItem)!!)
            ItemType.MEDICINE -> hotbarRepo.setMisc(invRepo.getIdOfItem(invItem)!!)
        }
    }
}