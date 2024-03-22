package com.team.app.data.repositories

import com.team.app.data.database.ItemsDao
import com.team.app.data.model.ItemType
import com.team.app.utils.Constants.Companion.INVALID_ITEM
import com.team.app.data.model.Item as ItemModel

class ItemsRepository(
    private val itemsDao: ItemsDao
) {
    suspend fun getItemByID(id: Int) : ItemModel {
        val item = itemsDao.getItem(id)
        if (item != null) {
            return ItemModel(
                ItemType.entries[item.type],
                item.name,
                item.price,
                item.actionValue
            )
        } else {
            return INVALID_ITEM
        }
    }

    suspend fun findByName(name: String) : Int? {
        return itemsDao.findByName(name)
    }
}