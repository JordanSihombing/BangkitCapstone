package com.choiri.bodybuddy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.choiri.bodybuddy.dataclass.LoginDataAccount
import com.choiri.bodybuddy.dataclass.RegisterDataAccount
import com.choiri.bodybuddy.dataclass.ResponseLogin
import com.choiri.bodybuddy.repository.MainRepository

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {


    val message: LiveData<String> = mainRepository.message

    val isLoading: LiveData<Boolean> = mainRepository.isLoading

    val userlogin: LiveData<ResponseLogin> = mainRepository.userlogin

    fun login(loginDataAccount: LoginDataAccount) {
        mainRepository.getResponseLogin(loginDataAccount)
    }

    fun register(registDataUser: RegisterDataAccount) {
        mainRepository.getResponseRegister(registDataUser)
    }

}
