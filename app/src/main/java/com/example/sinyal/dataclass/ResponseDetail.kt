package com.example.sinyal.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseDetail(

	@field:SerializedName("error")
	val error: Errors? = null,

	@field:SerializedName("message")
	val message: String? = null,

)

data class Errors(

	@field:SerializedName("name")
	val name: List<String?>? = null,

	@field:SerializedName("email")
	val email: List<String?>? = null,

	@field:SerializedName("username")
	val username: List<String?>? = null
)
