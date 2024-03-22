package com.team.app.data.repositories

import com.team.app.data.database.AttributesDao
import com.team.app.data.database.model.Attributes
import com.team.app.data.model.Attributes as AttributesModel
import com.team.app.utils.Constants.Companion.DEFAULT_COINS
import com.team.app.utils.Constants.Companion.DEFAULT_HAPPINESS
import com.team.app.utils.Constants.Companion.DEFAULT_HEALTH
import com.team.app.utils.Constants.Companion.DEFAULT_HUNGER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttributesRepository(
    private val attributesDao: AttributesDao
) {
//    suspend fun initAttributes() {
//        for (attr in attributesDao.getAttributes()) {
//            attributesDao.deleteAttributes(attr)
//        }
//
//        // DEFAULT VALUES
//        attributesDao.addAttributes(
//            Attributes(
//                id = 0,
//                coins = DEFAULT_COINS,
//                hunger = DEFAULT_HUNGER,
//                happiness = DEFAULT_HAPPINESS,
//                health = DEFAULT_HEALTH
//            )
//        )
//    }

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