package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class HorseManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "horse_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveHorseId(horseId: Int) {
        sharedPreferences.edit().putInt("horse_id", horseId).apply()
    }

    fun getHorseId(): Int {
        return sharedPreferences.getInt(
            "horse_id",
            -1
        )
    }

    fun clearHorseData() {
        sharedPreferences.edit().remove("horse_id").apply()
    }

    fun isHorseIdSaved(): Boolean {
        return getHorseId() != -1
    }
}