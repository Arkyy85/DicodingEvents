package com.ammar.core.data.source.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences(private val dataStore: DataStore<Preferences>) {

    private val lastFetchTimeKey = stringPreferencesKey("last_fetch_time")
    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    suspend fun saveLastFetchTime(time: Long) {
        val encryptedTime = XOREncryptionHelper.encrypt(time.toString()) // Enkripsi sebelum simpan
        dataStore.edit { preferences ->
            preferences[lastFetchTimeKey] = encryptedTime
        }
    }

    fun getLastFetchTime(): Flow<Long> {
        return dataStore.data.map { preferences ->
            val encryptedTime = preferences[lastFetchTimeKey]
            try {
                if (encryptedTime.isNullOrBlank()) {
                    0L // Nilai default jika kosong
                } else {
                    XOREncryptionHelper.decrypt(encryptedTime).toLong() // Dekripsi & ubah ke Long
                }
            } catch (e: Exception) {
                e.printStackTrace()
                0L // Gunakan nilai default jika terjadi error parsing
            }
        }
    }
}