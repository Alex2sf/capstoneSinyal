package com.example.sinyal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.UserPreferences
import com.example.sinyal.databinding.ActivityLoginBinding
import com.example.sinyal.dataclass.Errors
import com.example.sinyal.dataclass.LoginAccount
import com.example.sinyal.dataclass.ResponseLogin
import com.example.sinyal.repository.MainRepository
import com.example.sinyal.viewmodel.MainViewModel
import com.example.viewmodel.DataStoreViewModel
import com.example.viewmodel.ViewModelFactory
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import retrofit2.Call

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity() : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    // Membuat ViewModel yang akan digunakan
    lateinit var mainRepository: MainRepository

    private val loginViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menangani interaksi klik tombol dan menjalankan animasi
        ifClicked()
        playAnimation()

        // Mendapatkan preferensi pengguna dan ViewModel login
        val preferences = UserPreferences.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[DataStoreViewModel::class.java]

        // Mengamati sesi login
        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        // Mengamati pesan hasil login dan status loading
        loginViewModel.message.observe(this) { message ->
            responseLogin(
                message,
                dataStoreViewModel
            )
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    // Memainkan animasi
    private fun playAnimation() {
        // Membuat animasi perpindahan ikon login
        val iconLoginAnimator = ObjectAnimator.ofFloat(binding.login, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        // Daftar animasi lainnya
        val animators = listOf(
            ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.pass, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.passVisible, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(250),
            ObjectAnimator.ofFloat(binding.tvRegistDesc, View.ALPHA, 1f).setDuration(250)
        )

        // Menjalankan animasi
        AnimatorSet().apply {
            playTogether(iconLoginAnimator, *animators.toTypedArray())
            start()
        }
    }

    // Mengelola hasil login
    private fun responseLogin(
        message: String,
        dataStoreViewModel: DataStoreViewModel
    ) {
        val user = loginViewModel.userlogin.value
        user?.accessToken?.let { dataStoreViewModel.saveToken(it) }
        dataStoreViewModel.saveLoginSession(true)
        dataStoreViewModel.saveName("Hhehee")

        if (message.contains("Hello")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


        } else {

            // Menampilkan pesan toast
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Menangani klik tombol login dan memvalidasi data
    private fun ifClicked() {
        binding.btnLogin.setOnClickListener {
            binding.email.clearFocus()
            binding.pass.clearFocus()

            if (isDataValid()) {
                val requestLogin = LoginAccount(

                    binding.email.text.toString().trim(),
                    binding.pass.text.toString().trim()

                )
                loginViewModel.login(requestLogin, fun(response){

                    if (response.isSuccessful) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {

                        }

                },fun(t){
        })



            } else {
                if (!binding.email.emailValidationStatus) binding.email.error =
                    getString(R.string.emailNone)
                if (!binding.pass.passwordValidationStatus) binding.pass.error =
                    getString(R.string.passwordNone)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.passVisible.setOnCheckedChangeListener { _, isChecked ->
            binding.pass.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            // Set selection to end of text
            binding.pass.text?.let { binding.pass.setSelection(it.length) }
        }
    }

    // Memvalidasi data sebelum melakukan login
    private fun isDataValid(): Boolean {
        return binding.email.emailValidationStatus && binding.pass.passwordValidationStatus
    }

    // Menampilkan atau menyembunyikan indikator loading
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
