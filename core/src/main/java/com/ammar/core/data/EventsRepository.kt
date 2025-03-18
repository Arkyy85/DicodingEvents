package com.ammar.core.data

import com.ammar.core.data.source.local.LocalDataSource
import com.ammar.core.data.source.remote.RemoteDataSource
import com.ammar.core.data.source.remote.response.ListEventsItem
import com.ammar.core.data.source.remote.retrofit.ApiResponse
import com.ammar.core.domain.model.Events
import com.ammar.core.domain.repository.IEventsRepository
import com.ammar.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EventsRepository(private val remoteDataSource: RemoteDataSource ,
                       private val localDataSource: LocalDataSource,

):IEventsRepository {

    override fun getUpcomingEvents(): Flow<Resource<List<Events>>> =
        object : NetworkBoundResource<List<Events>, List<ListEventsItem>>() {
            override fun loadFromDB(): Flow<List<Events>> {
                return localDataSource.getEvents("upcoming").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Events>?): Boolean = data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<ListEventsItem>>> =
                remoteDataSource.getActiveEvents()

            override suspend fun saveCallResult(data: List<ListEventsItem>) {
                val eventEntities = DataMapper.mapResponsesToEntities(data, "upcoming")
                localDataSource.insertEvents(eventEntities)
            }
        }.asFlow()

    override fun getFinishedEvents(): Flow<Resource<List<Events>>> =
        object : NetworkBoundResource<List<Events>, List<ListEventsItem>>() {
            override fun loadFromDB(): Flow<List<Events>> {
                return localDataSource.getEvents("past").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Events>?): Boolean = data.isNullOrEmpty()


            override suspend fun createCall(): Flow<ApiResponse<List<ListEventsItem>>> =
                remoteDataSource.getFinishedEvents()

            override suspend fun saveCallResult(data: List<ListEventsItem>) {
                val eventEntities = DataMapper.mapResponsesToEntities(data, "past")
                localDataSource.insertEvents(eventEntities)
            }
        }.asFlow()

    override fun getSearchEvents(query: String?): Flow<Resource<List<Events>>> =
        object : NetworkBoundResource<List<Events>, List<ListEventsItem>>() {
            override fun loadFromDB(): Flow<List<Events>> {
                return localDataSource.getSearchEvents(query).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Events>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<ListEventsItem>>> =
                remoteDataSource.searchEvents(query)

            override suspend fun saveCallResult(data: List<ListEventsItem>) {}
        }.asFlow()

    override fun getFavoriteEvents(): Flow<List<Events>> {
        return localDataSource.getFavoriteEvents().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteEvents(event: Events, state: Boolean, eventType: String) {
        val eventEntity = DataMapper.mapDomainToEntity(event, eventType)
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.setFavoriteEvents(eventEntity, state)
        }
    }

    override fun getThemeSetting(): Flow<Boolean> {
        return localDataSource.getThemeSetting()
    }

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.saveThemeSetting(isDarkModeActive)
        }
    }
}