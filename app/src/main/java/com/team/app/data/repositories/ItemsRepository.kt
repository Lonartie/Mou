package com.team.app.data.repositories

import com.team.app.data.database.ItemsDao
import com.team.app.data.database.model.Item
import com.team.app.data.model.ItemType
import com.team.app.data.model.Item as ItemModel

class ItemsRepository(
    private val itemsDao: ItemsDao
) {
    suspend fun getItemByID(id: Int) : ItemModel {
        val item : Item = itemsDao.getItem(id)
        return ItemModel(
            ItemType.entries[item.type],
            item.name,
            item.price,
            item.actionValue
        )
    }

    suspend fun findByName(name: String) : Int {
        return itemsDao.findByName(name)
    }
}