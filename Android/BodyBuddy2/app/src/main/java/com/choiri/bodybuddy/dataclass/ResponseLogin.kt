package com.choiri.bodybuddy.dataclass

import com.google.gson.annotations.SerializedName

data class LoginDataAccount(
	var username: String,
	var password: String
)

data class ResponseLogin(

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("token")
	val token: String

)
