package com.team.app.data.repositories

import com.team.app.data.database.AttributesDao
import com.team.app.data.database.InventoryDao
import com.team.app.data.database.model.InventoryItem
import com.team.app.data.database.model.Item
import com.team.app.utils.toInventoryItem
import kotlinx.coroutines.flow.map
import com.team.app.data.model.InventoryItem as InventoryItemModel
import com.team.app.data.model.Item as ItemModel

class InventoryRepository(
    private val inventoryDao: InventoryDao,
    private val itemsRepo: ItemsRepository,
    private val attributesRepo: AttributesRepository
) {
    suspend fun getItemByID(id: Int): InventoryItemModel {
        val invItem: InventoryItem = inventoryDao.getItem(id)
        return InventoryItemModel(
            itemsRepo.getItemByID(invItem.itemID),
            invItem.quantity
        )
    }

    suspend fun removeOne(item: ItemModel) {
        val id = itemsRepo.findByName(item.name)
        val invID = inventoryDao.findByItemID(id)
        val invItem = inventoryDao.getItem(invID)
        val newInvItem = invItem.copy(quantity = invItem.quantity - 1)
        if (newInvItem.quantity == 0) {
            inventoryDao.removeItem(invItem)
        } else {
            inventoryDao.updateItem(newInvItem)
        }
    }

    suspend fun addOne(item: Item) {
        val userCoins = attributesRepo.getAttributes()[0].coins
        // check if user has enough money
        if (userCoins < item.price) return

        val invItem = inventoryDao.getItemByItemID(item.id)
        if (invItem == null) {
            inventoryDao.addItem(item.toInventoryItem(quantity = 1))
        } else {
            inventoryDao.updateItem(invItem.copy(quantity = invItem.quantity + 1))
        }
        // update coins
        attributesRepo.updateCoins(coins = userCoins - item.price)
    }

    fun getItemsFlow() = inventoryDao.getItemsFlow().map { invItem ->
        InventoryItemModel(
            itemsRepo.getItemByID(invItem.itemID),
            invItem.quantity
        )
    }
}