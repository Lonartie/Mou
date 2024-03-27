package com.team.app.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val DIE_COUNT = intPreferencesKey("die_count")
    }

    val getDieCountFlow = dataStore.data.map { preferences ->
        preferences[DIE_COUNT] ?: 0
    }

    suspend fun getDieCount(): Int {
        var data: Int
        runBlocking {
            data = dataStore.data.map { preferences ->
                preferences[DIE_COUNT] ?: 0
            }.first()
        }
        return data
    }

    suspend fun incDieCount() {
        dataStore.edit { preferences ->
            preferences[DIE_COUNT] = (preferences[DIE_COUNT] ?: 0) + 1
        }
    }
}