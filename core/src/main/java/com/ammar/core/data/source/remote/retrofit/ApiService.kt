package com.ammar.core.data.source.remote.retrofit

import com.ammar.core.data.source.remote.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1
    ): EventsResponse

    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): EventsResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String?
    ): EventsResponse
}
