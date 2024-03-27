package com.team.app.data.repositories

import com.team.app.data.database.HotbarDao
import com.team.app.data.model.Hotbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HotbarRepository(
    private val hotbarDao: HotbarDao,
    private val inventoryRepo: InventoryRepository
) {

    suspend fun getHotbar() : Hotbar {
        return hotbarDao.getItems()[0].let {
            Hotbar(
                foodItem = inventoryRepo.getItemByID(it.food),
                toyItem = inventoryRepo.getItemByID(it.toy),
                miscItem = inventoryRepo.getItemByID(it.misc)
            )
        }
    }

    suspend fun setFood(invID: Int) {
        val hotbar = hotbarDao.getItems()[0]
        hotbarDao.updateItem(hotbar.copy(food = invID))
        val nhotbar = hotbarDao.getItems()[0].food
    }

    suspend fun setToy(invID: Int) {
        val hotbar = hotbarDao.getItems()[0]
        hotbarDao.updateItem(hotbar.copy(toy = invID))
        val nhotbar = hotbarDao.getItems()[0].toy
    }

    suspend fun setMisc(invID: Int) {
        val hotbar = hotbarDao.getItems()[0]
        hotbarDao.updateItem(hotbar.copy(misc = invID))
        val nhotbar = hotbarDao.getItems()[0].misc
    }
}