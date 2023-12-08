package com.example.sinyal.api

import com.example.sinyal.dataclass.LoginDataAccount
import com.example.sinyal.dataclass.RegisterDataAccount
import com.example.sinyal.dataclass.ResponseDetail
import com.example.sinyal.dataclass.ResponseLogin


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST("register")
    fun registUser(@Body requestRegister: RegisterDataAccount): Call<ResponseDetail>

    @POST("login")
    fun loginUser(@Body requestLogin: LoginDataAccount): Call<ResponseLogin>



}