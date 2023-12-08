package com.example.sinyal.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.sinyal.R

class Email : AppCompatEditText, View.OnFocusChangeListener {

    var emailValidationStatus = false
    private lateinit var matchingEmail: String
    private var emailAlreadyTakenStatus = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // Mengatur latar belakang dengan border
        background = ContextCompat.getDrawable(context, R.drawable.border)

        // Mengatur tipe input menjadi alamat email
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        // Menambahkan onFocusChangeListener untuk memvalidasi alamat email
        onFocusChangeListener = this

        // Menambahkan TextWatcher untuk memvalidasi format alamat email saat teks berubah
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak melakukan apa-apa sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailValidation() // Memvalidasi format alamat email
                if (emailAlreadyTakenStatus) {
                    checkingIfEmailIsAlreadyRegistered() // Memvalidasi apakah alamat email sudah digunakan
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak melakukan apa-apa setelah teks berubah
            }
        })
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            emailValidation()
            if (emailAlreadyTakenStatus) {
                checkingIfEmailIsAlreadyRegistered()
            }
        }
    }

    fun assigningErrorMessage(message: String, email: String) {
        matchingEmail = email
        emailAlreadyTakenStatus = true
        val currentEmail = text.toString().trim()
        error = if (currentEmail == matchingEmail) {
            message
        } else {
            null
        }
    }

    private fun emailValidation() {
        emailValidationStatus = Patterns.EMAIL_ADDRESS.matcher(text.toString().trim()).matches()
        error = if (!emailValidationStatus) {
            resources.getString(R.string.emailFormatWrong)
        } else {
            null
        }
    }

    private fun checkingIfEmailIsAlreadyRegistered() {
        val email = text.toString().trim()
        error = if (emailAlreadyTakenStatus && email == matchingEmail) {
            resources.getString(R.string.emailTaken)
        } else {
            null
        }
    }
}
