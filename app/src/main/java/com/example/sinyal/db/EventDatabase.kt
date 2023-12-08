package com.example.sinyal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EventDetail::class, Remote::class],
    version = 2,
    exportSchema = false
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun getListStoryDetailDao(): EventDetailDao
    abstract fun getRemoteKeysDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "event_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}