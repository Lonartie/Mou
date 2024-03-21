package com.lonartie.bookdiary.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class SettingsRepository(val dataStore: DataStore<Preferences>) {

    private val KEY_OPTION_1 = booleanPreferencesKey("option1")

    val option1 = dataStore.data.map { preferences ->
        preferences[KEY_OPTION_1] ?: false
    }

    suspend fun saveOption1(option: Boolean) {
        dataStore.edit { settings ->
            settings[KEY_OPTION_1] = option
        }
    }

}