package com.example.sinyal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class Remote(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
