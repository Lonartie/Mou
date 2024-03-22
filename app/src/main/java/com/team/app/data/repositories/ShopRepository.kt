package com.team.app.data.repositories

import com.team.app.data.database.ItemDao
import com.team.app.data.database.model.OwnedItem

class ShopRepository(
    private val itemDao: ItemDao
) {
    suspend fun buyItem(ownedItem: OwnedItem) = itemDao.addItem(ownedItem)
}