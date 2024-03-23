package com.team.app.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val invRepo: InventoryRepository,
    private val hotbarRepo: HotbarRepository
) : ViewModel() {
    fun getItemsWithType(type: ItemType) = runBlocking {
        invRepo.getItems().filter {
            it.item.itemType == type
        }
    }

    fun setHotbarItem(invItem: InventoryItem) {
        viewModelScope.launch {
            when (invItem.item.itemType) {
                ItemType.FOOD -> hotbarRepo.setFood(invRepo.getIdOfItem(invItem)!!)
                ItemType.TOY -> hotbarRepo.setToy(invRepo.getIdOfItem(invItem)!!)
                ItemType.MISC -> hotbarRepo.setMisc(invRepo.getIdOfItem(invItem)!!)
                ItemType.MEDICINE -> {}
            }
        }
    }
}