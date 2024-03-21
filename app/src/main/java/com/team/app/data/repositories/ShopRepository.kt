package com.team.app.data.repositories

import com.team.app.data.database.ItemDao
import com.team.app.data.database.model.Item
import kotlinx.coroutines.flow.collect

class ShopRepository(
    private val itemDao: ItemDao
) {
    suspend fun buyItem(item: Item) {
        itemDao.getItemByType(item.type).collect {
            if (it.count > 1) {
                itemDao.updateItem(item)
            } else {
                itemDao.addItem(item)
            }
        }
    }
}