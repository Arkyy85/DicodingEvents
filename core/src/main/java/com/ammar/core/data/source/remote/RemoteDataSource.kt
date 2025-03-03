package com.ammar.core.data.source.remote

import android.util.Log
import com.ammar.core.data.source.remote.response.EventResponse
import com.ammar.core.data.source.remote.response.ListEventsItem
import com.ammar.core.data.source.remote.retrofit.ApiResponse
import com.ammar.core.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    fun getActiveEvents(): Flow<ApiResponse<List<ListEventsItem>>> {
        return fetchEvents { apiService.getActiveEvents() }
    }

    fun getFinishedEvents(): Flow<ApiResponse<List<ListEventsItem>>> {
        return fetchEvents { apiService.getFinishedEvents() }
    }

    fun searchEvents(query: String?): Flow<ApiResponse<List<ListEventsItem>>> {
        return fetchEvents { apiService.searchEvents(query = query) }
    }

    private fun fetchEvents(apiCall: suspend () -> EventResponse): Flow<ApiResponse<List<ListEventsItem>>> {
        return flow {
            try {
                val response = apiCall()
                val dataArray = response.listEvents
                if (!dataArray.isNullOrEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", "API Error: ${e.message}", e)
            }
        }.flowOn(Dispatchers.IO)
    }
}
