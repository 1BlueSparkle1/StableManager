package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class VeterinarianManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "veterinarian_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveVeterinarianId(veterinarianId: Int) {
        sharedPreferences.edit().putInt("veterinarian_id", veterinarianId).apply()
    }

    fun getVeterinarianId(): Int {
        return sharedPreferences.getInt(
            "veterinarian_id",
            -1
        )
    }

    fun clearVeterinarianData() {
        sharedPreferences.edit().remove("veterinarian_id").apply()
    }

    fun isVeterinarianIdSaved(): Boolean {
        return getVeterinarianId() != -1
    }
}