package com.team.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.team.app.data.database.model.InventoryItem
import com.team.app.data.database.model.Item
import com.team.app.data.model.Investment

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun String.capitalize(): String = this.lowercase().replaceFirstChar { it.uppercase() }

fun Investment.earningsFromSell(currentPrice: Double): Int {
    val coins = this.amount.toInt() * this.leverage
    val balance = coins * (currentPrice / this.price)
    return (balance - (this.amount * (this.leverage - 1)) - 5).toInt()
}

fun Int.toFormattedCoins(): String {
    val suffix = arrayOf("", "k", "Mio.")

    var num = this
    var i = 0
    while (num >= 1000) {
        num /= 1000
        i++
    }

    if (i >= suffix.size) return "much"

    return "$num${suffix[i]}"
}
