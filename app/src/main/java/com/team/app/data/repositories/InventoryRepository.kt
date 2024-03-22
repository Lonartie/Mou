package com.team.app.data.repositories

import com.team.app.data.database.InventoryDao
import com.team.app.data.database.model.InventoryItem
import com.team.app.data.model.InventoryItem as InventoryItemModel

class InventoryRepository(
    private val inventoryDao: InventoryDao,
    private val itemsRepo: ItemsRepository
) {
    suspend fun getItemByID(id: Int) : InventoryItemModel {
        val invItem : InventoryItem = inventoryDao.getItem(id)
        return InventoryItemModel(
            itemsRepo.getItemByID(invItem.itemID),
            invItem.quantity
        )
    }
}