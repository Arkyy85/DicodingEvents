package com.ammar.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ammar.core.data.source.local.entity.EventsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM events WHERE eventType = :eventType")
    fun getEventsList(eventType: String): Flow<List<EventsEntity>>

    @Query("SELECT * FROM events where isFav = 1")
    fun getFavoriteEvents(): Flow<List<EventsEntity>>

    @Query("SELECT * FROM events WHERE name LIKE '%' || :query || '%' COLLATE NOCASE")
    fun getSearchEvents(query: String?): Flow<List<EventsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvents(events: List<EventsEntity>)

    @Update
    suspend fun updateFavoriteEvents(events: EventsEntity)

    @Delete
    fun deleteEventsFromDB(events: EventsEntity)
}
