package com.team.app.data.repositories

import com.team.app.data.database.InventoryDao
import com.team.app.data.database.model.InventoryItem
import com.team.app.utils.Constants.Companion.INVALID_INVENTORY_ITEM
import kotlinx.coroutines.flow.map
import com.team.app.data.model.InventoryItem as InventoryItemModel
import com.team.app.data.model.Item as ItemModel

class InventoryRepository(
    private val inventoryDao: InventoryDao,
    private val itemsRepo: ItemsRepository,
    private val attributesRepo: AttributesRepository
) {
    suspend fun getItemByID(id: Int): InventoryItemModel {
        println("asked for item with id $id")
        val invItems = inventoryDao.getItems()
        println("invItems: $invItems")
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
        val id = itemsRepo.findIDByName(item.name) ?: return
        val invID = inventoryDao.findByItemID(id) ?: return
        val invItem = inventoryDao.getItem(invID) ?: return
        val newInvItem = invItem.copy(quantity = invItem.quantity - 1)
        if (newInvItem.quantity == 0) {
            inventoryDao.removeItem(invItem)
        } else {
            inventoryDao.updateItem(newInvItem)
        }
    }

    suspend fun addOne(item: ItemModel) {
        val userCoins = attributesRepo.getAttributes().coins

        // check if user has enough money
        if (userCoins < item.price) return

        val itemID = itemsRepo.findIDByName(item.name)
            ?: // this item doesn't exist in the database
            return

        val invItem = inventoryDao.getItemByItemID(itemID)

        if (invItem == null) {
            inventoryDao.addItem(InventoryItem(0, itemID, 1)) // lib will ignore id=0
        } else {
            inventoryDao.updateItem(invItem.copy(quantity = invItem.quantity + 1))
        }
        // update coins
        attributesRepo.updateCoins(coins = userCoins - item.price)
    }

    suspend fun getItems() = inventoryDao.getItems().map { invItem ->
        InventoryItemModel(
            itemsRepo.getItemByID(invItem.itemID),
            invItem.quantity
        )
    }

    suspend fun getIdOfItem(item: InventoryItemModel): Int? {
        return inventoryDao.getIDByName(item.item.name)
    }
}