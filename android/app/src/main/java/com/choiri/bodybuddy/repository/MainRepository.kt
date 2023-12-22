package com.choiri.bodybuddy.repository

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.choiri.bodybuddy.wrapEspressoIdlingResource
import com.choiri.bodybuddy.api.APIConfig
import com.choiri.bodybuddy.api.APIService
import com.choiri.bodybuddy.dataclass.LoginDataAccount
import com.choiri.bodybuddy.dataclass.RegisterDataAccount
import com.choiri.bodybuddy.dataclass.ResponseLogin
import com.choiri.bodybuddy.dataclass.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(
    private val apiService: APIService
) {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userLogin = MutableLiveData<ResponseLogin>()
    var userlogin: LiveData<ResponseLogin> = _userLogin

    fun getResponseLogin(loginDataAccount: LoginDataAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = APIConfig.getApiService().loginUser(loginDataAccount)
            api.enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()

                    if (response.isSuccessful) {
                        _userLogin.value = responseBody!!
                        _message.value = "Hello ${_userLogin.value!!.token}!"
                    } else {
                        when (response.code()) {
                            401 -> _message.value =
                                "Wrong Email or Password, Try Again"
                            408 -> _message.value =
                                "Low Internet Connection, Please Try Again"
                            else -> _message.value = "Pesan error: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Pesan error: " + t.message.toString()
                }

            })
        }
    }

    fun getResponseRegister(registDataUser: RegisterDataAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = APIConfig.getApiService().registUser(registDataUser)
            api.enqueue(object : Callback<ResponseRegister> {
                override fun onResponse(
                    call: Call<ResponseRegister>,
                    response: Response<ResponseRegister>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = "Create Account Success"

                    } else {
                        when (response.code()) {
                            400 -> _message.value =
                                "Username has been Taken, Please Try Again"
                            408 -> _message.value =
                                "Low Internet Connection, Please try Again"
                            else -> _message.value = "Pesan error: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Pesan error: " + t.message.toString()
                }

            })
        }
    }


}