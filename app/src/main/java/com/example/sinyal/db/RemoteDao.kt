package com.example.sinyal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<Remote>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteId(id: String): Remote?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemote()
}