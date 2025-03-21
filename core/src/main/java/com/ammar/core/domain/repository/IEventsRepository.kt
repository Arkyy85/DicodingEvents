package com.ammar.core.domain.repository

import com.ammar.core.data.Resource
import com.ammar.core.domain.model.Events
import kotlinx.coroutines.flow.Flow

interface IEventsRepository {

    fun getSearchEvents(query: String?): Flow<Resource<List<Events>>>

    fun getUpcomingEvents(): Flow<Resource<List<Events>>>

    fun getFinishedEvents(): Flow<Resource<List<Events>>>

    fun getFavoriteEvents(): Flow<List<Events>>

    fun setFavoriteEvents(event: Events, state: Boolean, eventType: String)

    fun getThemeSetting(): Flow<Boolean>

    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
}
