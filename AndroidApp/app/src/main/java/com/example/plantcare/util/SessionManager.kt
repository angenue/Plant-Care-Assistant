package com.example.plantcare.util

import android.content.Context

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        set(value) = sharedPreferences.edit().putBoolean("IS_LOGGED_IN", value).apply()
}
