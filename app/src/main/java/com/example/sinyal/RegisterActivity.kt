package com.example.sinyal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.UserPreferences
import com.example.sinyal.databinding.ActivityRegisterBinding
import com.example.sinyal.dataclass.LoginDataAccount
import com.example.sinyal.dataclass.RegisterDataAccount
import com.example.sinyal.viewmodel.MainViewModel
import com.example.viewmodel.DataStoreViewModel
import com.example.viewmodel.ViewModelFactory
import com.google.android.gms.ads.MobileAds

class RegisterActivity : AppCompatActivity() {
    // Mendeklarasikan variabel binding untuk binding layout dan view model
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: MainViewModel by lazy {
        // Mendapatkan instance dari MainViewModel menggunakan ViewModelProvider
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val loginViewModel: MainViewModel by lazy {
        // Mendapatkan instance dari MainViewModel untuk operasi login
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    // Fungsi yang dipanggil saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        // Menghubungkan layout XML dengan Activity menggunakan data binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Mengatur judul ActionBar dan tombol back
        supportActionBar?.title = resources.getString(R.string.createAccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Menginisialisasi listener untuk elemen UI yang di-klik
        ifClicked()

        // Mengakses dataStore dari UserPreferences dan mengamati status login
        val pref = UserPreferences.getInstance(dataStore)
        val dataStoreViewModel =      ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]

        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            // Jika sesi login aktif, pindah ke HomePageActivity dan tutup aktivitas ini
            if (sessionTrue) {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        // Mengamati pesan dari proses registrasi dan menanggapi hasilnya
        registerViewModel.message.observe(this) { messageRegist ->
            responseRegister(messageRegist)
        }

        // Mengamati pesan dari proses login dan menanggapi hasilnya
        loginViewModel.message.observe(this) { messageLogin ->
            responseLogin(messageLogin, dataStoreViewModel)
        }

        // Mengamati status loading dari ViewModel dan menampilkan ProgressBar sesuai keadaan
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    // Fungsi yang menanggapi hasil dari proses login
    private fun responseLogin(message: String, dataStoreViewModel: DataStoreViewModel) {
        // Jika pesan mengandung "Hello", login berhasil. Menyimpan sesi login dan informasi pengguna
        if (message.contains("Hello")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = loginViewModel.userlogin.value
            dataStoreViewModel.saveLoginSession(true)
            dataStoreViewModel.saveToken(user?.loginResult!!.token)
            dataStoreViewModel.saveName(user.loginResult.name)
        } else {
            // Jika login gagal, menampilkan pesan kesalahan
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi yang menanggapi hasil dari proses registrasi
    private fun responseRegister(message: String) {
        // Jika pesan registrasi berhasil, menampilkan pesan sukses dan mencoba untuk melakukan login
        if (message == "Yeay akun berhasil dibuat") {
            Toast.makeText(
                this,
                resources.getString(R.string.accountSuccessCreated),
                Toast.LENGTH_SHORT
            ).show()
            val userLogin = LoginDataAccount(
                binding.RegistEmail.text.toString(),
                binding.RegistPassword.text.toString()
            )
            loginViewModel.login(userLogin)
        } else {
            // Jika registrasi gagal, menampilkan pesan kesalahan
            if (message.contains("Email yang anda masukan sudah terdaftar")) {
                binding.RegistEmail.assigningErrorMessage(
                    resources.getString(R.string.emailTaken),
                    binding.RegistEmail.text.toString()
                )
                Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi yang menanggapi perubahan pada CheckBox untuk menampilkan/hide password
    private fun ifClicked() {
        binding.seeRegistPassword.setOnCheckedChangeListener { _, isChecked ->
            // Menampilkan atau menyembunyikan password berdasarkan status CheckBox
            if (isChecked) {
                binding.RegistPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.RetypePassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.RegistPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.RetypePassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

            // Menetapkan seleksi teks ke akhir teks
            binding.RegistPassword.text?.let { binding.RegistPassword.setSelection(it.length) }
            binding.RetypePassword.text?.let { binding.RetypePassword.setSelection(it.length) }
        }

        // Menanggapi klik pada tombol registrasi akun
        binding.btnRegistAccount.setOnClickListener {
            binding.apply {
                // Menghilangkan fokus dari semua elemen input
                RegistName.clearFocus()
                RegistEmail.clearFocus()
                RegistPassword.clearFocus()
                RetypePassword.clearFocus()
            }

            // Memvalidasi input pengguna dan melakukan registrasi jika valid
            if (binding.RegistName.nameValidationStatus && binding.RegistEmail.emailValidationStatus && binding.RegistPassword.passwordValidationStatus && binding.RetypePassword.passwordValidationStatus) {
                val dataRegisterAccount = RegisterDataAccount(
                    name = binding.RegistName.text.toString().trim(),
                    email = binding.RegistEmail.text.toString().trim(),
                    password = binding.RegistPassword.text.toString().trim()
                )

                // Memanggil fungsi registrasi dari ViewModel
                registerViewModel.register(dataRegisterAccount)
            } else {
                // Menampilkan pesan kesalahan jika input tidak valid
                if (!binding.RegistName.nameValidationStatus) binding.RegistName.error =
                    resources.getString(R.string.nameNone)
                if (!binding.RegistEmail.emailValidationStatus) binding.RegistEmail.error =
                    resources.getString(R.string.emailNone)
                if (!binding.RegistPassword.passwordValidationStatus) binding.RegistPassword.error =
                    resources.getString(R.string.passwordNone)
                if (!binding.RetypePassword.passwordValidationStatus) binding.RetypePassword.error =
                    resources.getString(R.string.passwordConfirmNone)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk menampilkan atau menyembunyikan ProgressBar berdasarkan status isLoading
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Fungsi yang menangani aksi saat tombol back di ActionBar ditekan
    override fun onSupportNavigateUp(): Boolean {
        // Menutup aktivitas dan kembali ke aktivitas sebelumnya
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}
