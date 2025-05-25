package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class GenderHorseManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "gender_horse_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveGenderHorseId(genderHorseId: Int) {
        sharedPreferences.edit().putInt("gender_horse_id", genderHorseId).apply()
    }

    fun getGenderHorseId(): Int {
        return sharedPreferences.getInt(
            "gender_horse_id",
            -1
        )
    }

    fun clearGenderHorseData() {
        sharedPreferences.edit().remove("gender_horse_id").apply()
    }

    fun isGenderHorseIdSaved(): Boolean {
        return getGenderHorseId() != -1
    }
}