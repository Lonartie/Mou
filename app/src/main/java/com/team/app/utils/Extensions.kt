package com.team.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.team.app.data.database.model.InventoryItem
import com.team.app.data.database.model.Item
import com.team.app.data.model.Investment

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Item.toInventoryItem(quantity: Int): InventoryItem {
    return InventoryItem(
        itemID = id,
        quantity = quantity
    )
}

fun String.capitalize(): String = this.lowercase().replaceFirstChar { it.uppercase() }


fun Investment.earningsFromSell(currentPrice: Double): Int {
    val coins = this.amount.toInt() * this.leverage
    val balance = coins * (currentPrice / this.price)
    return (balance - (this.amount * (this.leverage - 1)) - 5).toInt()
}
