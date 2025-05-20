package com.example.stablemanager.Components

import android.widget.EditText

fun EditText.setEditable(editable: Boolean) {
    isClickable = editable
    isCursorVisible = editable
    isFocusable = editable
    isFocusableInTouchMode = editable
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return email.matches(emailRegex)
}