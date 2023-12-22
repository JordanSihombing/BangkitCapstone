package com.choiri.bodybuddy.dataclass

import com.google.gson.annotations.SerializedName


data class RegisterDataAccount(
	var username: String,
	var password: String
)
data class ResponseRegister(
	@field:SerializedName("message")
	val message: String
)
