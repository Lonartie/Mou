package com.team.app.ui.inventory

import androidx.lifecycle.ViewModel
import com.team.app.data.model.ItemType
import com.team.app.data.repositories.HotbarRepository
import com.team.app.data.repositories.InventoryRepository
import com.team.app.data.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepo: InventoryRepository,
    private val itemsRepo: ItemsRepository,
    private val hotbarRepo: HotbarRepository
) : ViewModel() {
    fun getItemsWithType(type: ItemType) = runBlocking {
        inventoryRepo.getItems().filter {
            itemsRepo.getItemByID(it.itemID).itemType == type
        }
    }

    fun getItemWithID(id: Int) = runBlocking {
       itemsRepo.getItemByID(id)
    }
}