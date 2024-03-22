package com.team.app.utils

import androidx.annotation.DrawableRes
import com.team.app.R

class Constants {
    companion object {

        val ITEM_NAME_TO_RESOURCE_MAP = mapOf(
            "Chicken" to Pair(R.drawable.chicken_leg, 1.75f),
            "Mouse" to Pair(R.drawable.toy_mouse, 1.2f),
            "Health Potion" to Pair(R.drawable.potion, 1.2f),
        )

        @DrawableRes fun getItemResource(name: String) = ITEM_NAME_TO_RESOURCE_MAP[name]?.first ?: R.drawable.coins
        fun getItemScalingFactor(name: String) = ITEM_NAME_TO_RESOURCE_MAP[name]?.second ?: 1f
    }
}