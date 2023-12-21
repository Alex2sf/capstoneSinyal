package com.example.sinyal.dataclass

import com.google.gson.annotations.SerializedName

data class LoginResult(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("user")
	val user: User? = null

)

data class RegisterDataAccount(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	var password: String? = null,

)
