package com.team.app.data.repositories

import com.team.app.data.database.HotbarDao
import com.team.app.data.model.Hotbar
import kotlinx.coroutines.flow.map

class HotbarRepository(
    private val hotbarDao: HotbarDao,
    private val inventoryRepo: InventoryRepository
) {
    fun getHotbar() = hotbarDao.getItemsFlow().map { hotbar ->
        Hotbar(
            foodItem = inventoryRepo.getItemByID(hotbar.food),
            toyItem = inventoryRepo.getItemByID(hotbar.toy),
            miscItem = inventoryRepo.getItemByID(hotbar.misc)
        )
    }
}