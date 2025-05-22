package com.example.stablemanager.Components

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class StableManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "stable_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveStableId(stableId: Int) {
        sharedPreferences.edit().putInt("stable_id", stableId).apply()
    }

    fun getStableId(): Int {
        return sharedPreferences.getInt(
            "stable_id",
            -1
        )
    }

    fun clearStableData() {
        sharedPreferences.edit().remove("stable_id").apply()
    }

    fun isStableIdSaved(): Boolean {
        return getStableId() != -1
    }
}