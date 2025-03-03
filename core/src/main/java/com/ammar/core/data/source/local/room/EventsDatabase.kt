package com.ammar.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ammar.core.data.source.local.entity.EventsEntity

@Database(entities = [EventsEntity::class], version = 3, exportSchema = false)
abstract class EventsDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao
}
