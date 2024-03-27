package com.team.app.data.repositories

import com.team.app.data.database.ItemsDao
import com.team.app.data.model.ItemType
import com.team.app.utils.Constants.Companion.INVALID_ITEM
import kotlinx.coroutines.flow.map
import com.team.app.data.model.Item as ItemModel

class ItemsRepository(
    private val itemsDao: ItemsDao
) {
    suspend fun getItemByID(id: Int): ItemModel {
        val item = itemsDao.getItem(id)
        return if (item != null) {
            ItemModel(
                ItemType.entries[item.type],
                item.name,
                item.price,
                item.actionValue
            )
        } else {
            INVALID_ITEM
        }
    }

    fun getItemsNoInvalidFlow() = itemsDao.getSortedItemsNoInvalidFlow()
        .map { items ->
            items.map {
                ItemModel(
                    ItemType.entries[it.type],
                    it.name,
                    it.price,
                    it.actionValue
                )
            }
        }

    suspend fun findIDByName(name: String): Int? {
        return itemsDao.findByName(name)
    }
}