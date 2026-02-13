package com.example.bmicalculator.auth

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

class AuthManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bmi_session", Context.MODE_PRIVATE)

    val db = AuthDatabase.getInstance(context).userDao()

    companion object {
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ID = "user_id"

        fun hashPassword(input: String): String {
            val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray(Charsets.UTF_8))
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

    fun saveSession(user: UserEntity) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_USER_NAME, user.name)
            .putInt(KEY_USER_ID, user.id)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)
    fun getLoggedInEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    fun getLoggedInUserName(): String = prefs.getString(KEY_USER_NAME, "User") ?: "User"

    fun logout() {
        prefs.edit().clear().apply()
    }
}
