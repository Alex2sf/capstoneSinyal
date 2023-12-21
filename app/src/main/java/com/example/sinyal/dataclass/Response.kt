package com.example.sinyal.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,


)
