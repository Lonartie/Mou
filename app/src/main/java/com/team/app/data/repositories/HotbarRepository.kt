package com.team.app.data.repositories

import com.team.app.data.database.HotbarDao
import com.team.app.data.model.Hotbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HotbarRepository(
    private val hotbarDao: HotbarDao,
    private val inventoryRepo: InventoryRepository
) {
    fun getItemsFlow() = hotbarDao.getItemsFlow().map { hotbar ->
        Hotbar(
            foodItem = inventoryRepo.getItemByID(hotbar.food),
            toyItem = inventoryRepo.getItemByID(hotbar.toy),
            miscItem = inventoryRepo.getItemByID(hotbar.misc)
        )
    }

    suspend fun getHotbar() : Hotbar {
        val hotbar = hotbarDao.getItems()
        return Hotbar(
            foodItem = inventoryRepo.getItemByID(hotbar[0].food),
            toyItem = inventoryRepo.getItemByID(hotbar[0].toy),
            miscItem = inventoryRepo.getItemByID(hotbar[0].misc)
        )
    }

    fun getHotbarFlow() : Flow<Hotbar> {
        return hotbarDao.getItemsFlow().map {
            Hotbar(
                foodItem = inventoryRepo.getItemByID(it.food),
                toyItem = inventoryRepo.getItemByID(it.toy),
                miscItem = inventoryRepo.getItemByID(it.misc)
            )
        }
    }
}