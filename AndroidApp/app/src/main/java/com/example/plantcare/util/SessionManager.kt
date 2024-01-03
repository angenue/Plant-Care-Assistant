package com.example.plantcare.util

import android.content.Context

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "encrypted_app_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        set(value) = sharedPreferences.edit().putBoolean("IS_LOGGED_IN", value).apply()

    fun saveAuthHeader(authHeader: String) {
        sharedPreferences.edit().putString("AuthHeader", authHeader).apply()
    }

    fun getAuthHeader(): String? {
        return sharedPreferences.getString("AuthHeader", null)
    }

    fun clearAuthHeader() {
        sharedPreferences.edit().remove("AuthHeader").apply()
    }
}


