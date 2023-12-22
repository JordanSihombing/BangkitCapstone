package com.choiri.bodybuddy.api

import com.choiri.bodybuddy.dataclass.LoginDataAccount
import com.choiri.bodybuddy.dataclass.RegisterDataAccount
import com.choiri.bodybuddy.dataclass.ResponseLogin
import com.choiri.bodybuddy.dataclass.ResponseRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("register")
    fun registUser(@Body requestRegister: RegisterDataAccount): Call<ResponseRegister>

    @POST("login")
    fun loginUser(@Body requestLogin: LoginDataAccount): Call<ResponseLogin>



}