package com.example.sinyal.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseLoginBla(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("loginResult")
	var loginResult: LoginResult

)
