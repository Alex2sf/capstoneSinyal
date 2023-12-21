package com.example.sinyal.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class EventDetail(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)



@Entity(tableName = "data")
data class Data(

	@PrimaryKey
	@ColumnInfo(name = "id")
	val id: String,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("start_time")
	val startTime: Int? = null,

	@field:SerializedName("end_time")
	val endTime: Int? = null,

	@field:SerializedName("max_participant")
	val maxParticipant: Int? = null,

	@field:SerializedName("thumbnail_url")
	val thumbnailUrl: String? = null,

	@field:SerializedName("lon")
	val lon: Float? = null,

	@field:SerializedName("lat")
	val lat: Float? = null
)
