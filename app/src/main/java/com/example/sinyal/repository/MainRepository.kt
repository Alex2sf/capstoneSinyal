package com.example.sinyal.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sinyal.api.APIConfig
import com.example.sinyal.api.APIService
import com.example.sinyal.dataclass.LoginDataAccount
import com.example.sinyal.dataclass.RegisterDataAccount
import com.example.sinyal.dataclass.ResponseDetail
import com.example.sinyal.dataclass.ResponseLogin
import com.example.sinyal.db.EventDatabase
import com.example.sinyal.db.EventDetail
import com.example.sinyal.wrapEspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(
    private val storyDatabase: EventDatabase,
    private val apiService: APIService
) {

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loading

    private val _userLogin = MutableLiveData<ResponseLogin>()
    var userlogin: LiveData<ResponseLogin> = _userLogin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _stories = MutableLiveData<List<EventDetail>>()
    var stories: LiveData<List<EventDetail>> = _stories


    fun getResponseLogin(loginDataAccount: LoginDataAccount) {
        wrapEspressoIdlingResource {
            _loading.value = true
            val api = APIConfig.getApiService().loginUser(loginDataAccount)
            api.enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    _loading.value = false
                    val responseBody = response.body()

                    if (response.isSuccessful) {
                        _userLogin.value = responseBody!!
                        _message.value = "Hello ${_userLogin.value!!.loginResult.name}!"
                    } else {
                        when (response.code()) {
                            401 -> _message.value =
                                "Email atau password yang anda masukan salah, silahkan coba lagi"
                            408 -> _message.value =
                                "Koneksi internet anda lambat, silahkan coba lagi"
                            else -> _message.value = "Gagal login. Kode HTTP: ${response.code()}" // Perubahan: Pesan kesalahan HTTP
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    _loading.value = false
                    _message.value = "Gagal login. ${t.message}" // Perubahan: Pesan kesalahan saat koneksi gagal
                }

            })
        }
    }

    fun getResponseRegister(registDataUser: RegisterDataAccount) {
        wrapEspressoIdlingResource {
            _loading.value = true
            val api = APIConfig.getApiService().registUser(registDataUser)
            api.enqueue(object : Callback<ResponseDetail> {
                override fun onResponse(
                    call: Call<ResponseDetail>,
                    response: Response<ResponseDetail>
                ) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        _message.value = "Yeay akun berhasil dibuat"
                    } else {
                        when (response.code()) {
                            400 -> _message.value =
                                "Email yang anda masukan sudah terdaftar, silahkan coba lagi"
                            408 -> _message.value =
                                "Koneksi internet anda lambat, silahkan coba lagi"
                            else -> _message.value = "Gagal login. Kode HTTP: ${response.code()}" // Perubahan: Pesan kesalahan HTTP
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                    _loading.value = false
                    _message.value = "Gagal login. ${t.message}" // Perubahan: Pesan kesalahan saat koneksi gagal
                }

            })
        }
    }




}