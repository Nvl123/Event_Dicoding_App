package com.dicoding.mysubmision1novil.data.settings


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val  dataStore: DataStore<Preferences>){

    private val THEME_KEY = booleanPreferencesKey("theme_settings")

    fun getThemeSettings() : Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    }

    suspend fun saveThemeSettings(isDarkModeActive : Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {

        @Volatile
        private var INSTANCE : SettingPreferences? = null

        fun getInstance (dataStore: DataStore<Preferences>): SettingPreferences{
            return  INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }
}