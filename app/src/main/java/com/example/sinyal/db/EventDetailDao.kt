package com.example.sinyal.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(stories: List<EventDetail>)

    @Query("SELECT * FROM events")
    fun getAllStories(): PagingSource<Int, EventDetail>

    @Query("SELECT * FROM events")
    fun getAllListEvent(): List<EventDetail>

    @Query("DELETE FROM events")
    suspend fun deleteAll()

}