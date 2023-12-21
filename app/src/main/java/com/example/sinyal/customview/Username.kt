package com.example.sinyal.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.sinyal.R

class Username : AppCompatEditText, View.OnFocusChangeListener {

    var userValidationStatus = false

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

        // Mengatur tipe input menjadi teks
        inputType = InputType.TYPE_CLASS_TEXT

        // Menambahkan onFocusChangeListener untuk memvalidasi nama
        onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            nameValidation()
        }
    }

    private fun nameValidation() {
        userValidationStatus = text.toString().trim().isNotEmpty()
        error = if (!userValidationStatus) {
            resources.getString(R.string.nameNone)
        } else {
            null
        }
    }
}