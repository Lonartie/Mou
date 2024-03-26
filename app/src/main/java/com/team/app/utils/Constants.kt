package com.team.app.utils

import androidx.annotation.DrawableRes
import com.team.app.R
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType

class Constants {
    companion object {

        const val STOCKS_API_BASE = "https://api.twelvedata.com/"

        val ITEM_NAME_TO_RESOURCE_MAP = mapOf(
            "Chicken" to Pair(R.drawable.chicken_leg, 1.75f),
            "Mouse" to Pair(R.drawable.toy_mouse, 1.2f),
            "Health Potion" to Pair(R.drawable.potion, 1.2f),
        )

        @DrawableRes fun getItemResource(name: String) = ITEM_NAME_TO_RESOURCE_MAP[name]?.first ?: R.drawable.red_x
        fun getItemScalingFactor(name: String) = ITEM_NAME_TO_RESOURCE_MAP[name]?.second ?: 1f

        val INVALID_ITEM = Item(ItemType.MISC, "", 0, 0)
        val INVALID_INVENTORY_ITEM = InventoryItem(INVALID_ITEM, 0)

        const val MAX_HUNGER = 100
        const val MAX_HAPPINESS = 100
        const val MAX_HEALTH = 100

        const val STEP_COUNTER_TAG = "STEP_COUNT_LISTENER"
    }
}