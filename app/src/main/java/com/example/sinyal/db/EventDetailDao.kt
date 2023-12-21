package com.example.sinyal.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sinyal.dataclass.Data

@Dao
interface EventDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(stories: List<Data>)

    @Query("SELECT * FROM data")
    fun getAllStories(): PagingSource<Int, Data>

    @Query("SELECT * FROM data")
    fun getAllListEvent(): List<Data>

    @Query("DELETE FROM data")
    suspend fun deleteAll()

}