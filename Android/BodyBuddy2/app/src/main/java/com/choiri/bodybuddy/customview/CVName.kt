package com.choiri.bodybuddy.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.choiri.bodybuddy.R

class CVName : AppCompatEditText, View.OnFocusChangeListener {

    var isNameValid = false
    private lateinit var emailSame: String
    private var isEmailHasTaken = false

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
        // Set background with border

        // Set input type to text
        inputType = InputType.TYPE_CLASS_TEXT

        // Set onFocusChangeListener to validate name
        onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validateName()
        }
    }

    private fun validateName() {
        isNameValid = text.toString().trim().isNotEmpty()
        error = if (!isNameValid) {
            resources.getString(R.string.nameNone)
        } else {
            null
        }
    }

    fun setErrorMessage(message: String, email: String) {
        emailSame = email
        isEmailHasTaken = true
        error = if (text.toString().trim() == emailSame) {
            message
        } else {
            null
        }
    }
}
