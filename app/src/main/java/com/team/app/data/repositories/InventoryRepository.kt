package com.team.app.data.repositories

import com.team.app.data.database.InventoryDao
import com.team.app.utils.Constants.Companion.INVALID_INVENTORY_ITEM
import kotlinx.coroutines.flow.map
import com.team.app.data.model.InventoryItem as InventoryItemModel
import com.team.app.data.model.Item as ItemModel

class InventoryRepository(
    private val inventoryDao: InventoryDao,
    private val itemsRepo: ItemsRepository
) {
    suspend fun getItemByID(id: Int): InventoryItemModel {
        val invItem = inventoryDao.getItem(id)
        if (invItem != null) {
            return InventoryItemModel(
                itemsRepo.getItemByID(invItem.itemID),
                invItem.quantity
            )
        } else {
            return INVALID_INVENTORY_ITEM
        }
    }

    suspend fun removeOne(item: ItemModel) {
        val id = itemsRepo.findByName(item.name)!!
        val invID = inventoryDao.findByItemID(id)!!
        val invItem = inventoryDao.getItem(invID)!!
        val newInvItem = invItem.copy(quantity = invItem.quantity - 1)
        if (newInvItem.quantity == 0) {
            inventoryDao.removeItem(invItem)
        } else {
            inventoryDao.updateItem(newInvItem)
        }
    }

    fun getItemsFlow() = inventoryDao.getItemsFlow().map { invItem ->
        InventoryItemModel(
            itemsRepo.getItemByID(invItem.itemID),
            invItem.quantity
        )
    }

    suspend fun getItems() = inventoryDao.getItems()
}