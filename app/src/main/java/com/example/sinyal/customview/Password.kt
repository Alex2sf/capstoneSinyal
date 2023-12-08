package com.example.sinyal.customview

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.sinyal.R

class Password : AppCompatEditText, View.OnTouchListener {

    var passwordValidationStatus: Boolean = false

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

        // Menambahkan pemantau perubahan teks
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak melakukan apa-apa
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Memvalidasi kata sandi saat teks berubah
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak melakukan apa-apa
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            validatePassword()
        }
    }

    private fun validatePassword() {
        passwordValidationStatus = (text?.length ?: 0) >= 8
        error = if (!passwordValidationStatus) {
            resources.getString(R.string.passwordLess)
        } else {
            null
        }
    }
}
