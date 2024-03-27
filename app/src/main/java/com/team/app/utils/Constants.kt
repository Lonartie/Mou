package com.team.app.utils

import androidx.annotation.DrawableRes
import com.team.app.R
import com.team.app.data.model.InventoryItem
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType

class Constants {
    companion object {

        const val STOCKS_API_BASE = "https://api.twelvedata.com/"

        private val ITEM_NAME_TO_RESOURCE_MAP = mapOf(
            "Chicken" to R.drawable.chicken_leg,
            "Mouse" to R.drawable.toy_mouse,
            "Health Potion" to R.drawable.potion,
            "Coke" to R.drawable.food_1_cola,
            "Burger" to R.drawable.food_2_burger,
            "Pills" to R.drawable.healt_item_1_pills,
            "Plaster" to R.drawable.health_item_2_plaster,
            "Medipack" to R.drawable.health_item_3_medipack,
            "PS5" to R.drawable.toy_1_ps5,
            "Ball" to R.drawable.toy_2_ball,
            "Chalk" to R.drawable.toy_3_chalk,
        )

        @DrawableRes fun getItemResource(name: String) = ITEM_NAME_TO_RESOURCE_MAP[name] ?: R.drawable.red_x

        val INVALID_ITEM = Item(ItemType.MISC, "", 0, 0)
        val INVALID_INVENTORY_ITEM = InventoryItem(INVALID_ITEM, 0)

        const val MAX_HUNGER = 100
        const val MAX_HAPPINESS = 100
        const val MAX_HEALTH = 100

        const val TIC_TAC_TOE_PRIZE = 50

        const val STEP_COUNTER_TAG = "STEP_COUNT_LISTENER"

        // Permissions
        const val PERMISSION_DECLINED = "You declined the permission permanently. " +
                "Please go to your settings to grant it manually" +
                "After granting the permission please restart the app to proceed"

        const val NOTIFICATION_INFO = "We need this permission to send you notifications for various reasons, like \n" +
                "reminder too feed your mini-me or if you got coins to collect, its required"
        const val TRACK_STEPS_INFO = "We need this permission to keep accurate progress of your steps \n" +
                "its required"
        const val MOVEMENT_INFO = "We need this permission to keep accurate progress of your steps \n" +
                "its required"
    }
}