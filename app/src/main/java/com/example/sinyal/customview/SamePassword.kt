package com.example.sinyal.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.sinyal.R

class SamePassword : AppCompatEditText, View.OnFocusChangeListener {

    var passwordValidationStatus = false

    init {
        init()
    }

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
        transformationMethod = PasswordTransformationMethod.getInstance()

        // Menambahkan pemantau perubahan fokus
        onFocusChangeListener = this

        // Menambahkan pemantau perubahan teks
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak melakukan apa-apa
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordValidation()
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak melakukan apa-apa
            }
        })
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            passwordValidation()
        }
    }

    private fun passwordValidation() {
        val password = text.toString().trim()
        val confirmPassword =
            (parent as ViewGroup).findViewById<Password>(R.id.RegistPassword).text.toString()
                .trim()

        passwordValidationStatus = password.length >= 8 && password == confirmPassword
        error = if (!passwordValidationStatus) {
            resources.getString(R.string.passwordNotMatch)
        } else {
            null
        }
    }
}
