package com.team.app.data.repositories

import com.team.app.data.database.AttributesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.team.app.data.model.Attributes as AttributesModel

class AttributesRepository(
    private val attributesDao: AttributesDao
) {
    fun getAttributes(): Flow<AttributesModel> {
        return attributesDao.getAttributesFlow().map { attr ->
            AttributesModel(
                coins = attr.coins,
                hunger = attr.hunger,
                happiness = attr.happiness,
                health = attr.health
            )
        }
    }

    suspend fun updateCoins(coins: Int) {
        val attr = attributesDao.getAttributes()[0]
        attributesDao.updateAttributes(attr.copy(coins = coins))
    }

    suspend fun updateHunger(hunger: Int) {
        val attr = attributesDao.getAttributes()[0]
        attributesDao.updateAttributes(attr.copy(hunger = hunger))
    }

    suspend fun updateHappiness(happiness: Int) {
        val attr = attributesDao.getAttributes()[0]
        attributesDao.updateAttributes(attr.copy(happiness = happiness))
    }

    suspend fun updateHealth(health: Int) {
        val attr = attributesDao.getAttributes()[0]
        attributesDao.updateAttributes(attr.copy(health = health))
    }
}