package com.example.sinyal.dataclass

import com.google.gson.annotations.SerializedName

data class LoginAccount(

	@field:SerializedName("email")
	val email: String? = null,


	@field:SerializedName("password")
	var password: String? = null,

	)
