package com.lonartie.bookdiary.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.app.data.model.Attributes
import com.team.app.data.model.Item
import com.team.app.data.model.ItemType
import kotlinx.coroutines.flow.map

class SettingsRepository(val dataStore: DataStore<Preferences>) {

    private val CURRENT_FOOD_KEY = stringPreferencesKey("CurrentFood")
    private val CURRENT_TOY_KEY = stringPreferencesKey("CurrentToy")
    private val CURRENT_MISC_KEY = stringPreferencesKey("CurrentMisc")

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val jsonAttributesAdapter: JsonAdapter<Attributes> = moshi.adapter(Attributes::class.java)
    private val jsonItemAdapter: JsonAdapter<Item> = moshi.adapter(Item::class.java)

    val currentFood = dataStore.data.map { preferences ->
        try {
            jsonItemAdapter.fromJson(preferences[CURRENT_FOOD_KEY] ?: "") ?: Item(ItemType.FOOD, "", 0, 0)
        } catch (e: Exception) {
            Item(ItemType.FOOD, "", 0, 0)
        }
    }

    val currentToy = dataStore.data.map { preferences ->
        try {
            jsonItemAdapter.fromJson(preferences[CURRENT_TOY_KEY] ?: "") ?: Item(ItemType.TOY, "", 0, 0)
        } catch (e: Exception) {
            Item(ItemType.TOY, "", 0, 0)
        }
    }

    val currentMisc = dataStore.data.map { preferences ->
        try {
            jsonItemAdapter.fromJson(preferences[CURRENT_MISC_KEY] ?: "") ?: Item(ItemType.MISC, "", 0, 0)
        } catch (e: Exception) {
            Item(ItemType.MISC, "", 0, 0)
        }
    }

    suspend fun saveCurrentItem(item: Item) {
        val key = when (item.itemType) {
            ItemType.FOOD -> CURRENT_FOOD_KEY
            ItemType.TOY -> CURRENT_TOY_KEY
            ItemType.MISC -> CURRENT_MISC_KEY
            ItemType.MEDICINE -> CURRENT_MISC_KEY
        }

        dataStore.edit { settings ->
            settings[key] = jsonItemAdapter.toJson(item)
        }
    }

}