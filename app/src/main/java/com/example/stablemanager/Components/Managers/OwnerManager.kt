package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class OwnerManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "owner_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveOwnerId(ownerId: Int) {
        sharedPreferences.edit().putInt("owner_id", ownerId).apply()
    }

    fun getOwnerId(): Int {
        return sharedPreferences.getInt(
            "owner_id",
            -1
        )
    }

    fun clearOwnerData() {
        sharedPreferences.edit().remove("owner_id").apply()
    }

    fun isOwnerIdSaved(): Boolean {
        return getOwnerId() != -1
    }
}