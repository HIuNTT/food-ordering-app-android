package com.example.shopbtlkotlin.util

import android.text.TextUtils
import android.util.Patterns

class Validate {

    companion object {
        fun isValidPassword(pass: String): Boolean {
            return !TextUtils.isEmpty(pass)
        }

        fun isValidUsername(username: String): Boolean {
            return !TextUtils.isEmpty(username)
        }

        fun isValidEmail(email: String): Boolean {
            return if (TextUtils.isEmpty(email)) false else Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}