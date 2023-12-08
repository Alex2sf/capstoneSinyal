package com.example.sinyal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.example.sinyal.dataclass.LoginDataAccount
import com.example.sinyal.dataclass.RegisterDataAccount
import com.example.sinyal.dataclass.ResponseLogin
import com.example.sinyal.repository.MainRepository


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
