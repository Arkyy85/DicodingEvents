package com.ammar.core.data.source.local

import com.ammar.core.data.source.local.entity.EventsEntity
import com.ammar.core.data.source.local.preference.SettingPreferences
import com.ammar.core.data.source.local.room.EventsDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val eventsDao: EventsDao, private val preferences: SettingPreferences) {

    fun getEvents(eventType: String): Flow<List<EventsEntity>> = eventsDao.getEventsList(eventType)

    fun getFavoriteEvents(): Flow<List<EventsEntity>> = eventsDao.getFavoriteEvents()

    fun getSearchEvents(query: String?): Flow<List<EventsEntity>> = eventsDao.getSearchEvents(query)

    fun getThemeSetting() :Flow<Boolean> = preferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = preferences.saveThemeSetting(isDarkModeActive)

    suspend fun insertEvents(eventsList: List<EventsEntity>) = eventsDao.addEvents(eventsList)

    suspend fun setFavoriteEvents(events: EventsEntity, newState: Boolean) {
        events.isFav = newState
        eventsDao.updateFavoriteEvents(events)
    }

    suspend fun saveLastFetchTime(time: Long) = preferences.saveLastFetchTime(time)

    fun getLastFetchTime(): Flow<Long> = preferences.getLastFetchTime()
}