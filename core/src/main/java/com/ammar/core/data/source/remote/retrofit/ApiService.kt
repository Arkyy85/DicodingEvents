package com.ammar.core.data.source.remote.retrofit

import com.ammar.core.data.source.remote.response.EventResponse
import retrofit2.http.*

interface ApiService {

    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String?
    ): EventResponse
}