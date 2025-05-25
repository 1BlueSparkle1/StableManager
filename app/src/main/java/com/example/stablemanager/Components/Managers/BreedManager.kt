package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class BreedManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "breed_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveBreedId(breedId: Int) {
        sharedPreferences.edit().putInt("breed_id", breedId).apply()
    }

    fun getBreedId(): Int {
        return sharedPreferences.getInt(
            "breed_id",
            -1
        )
    }

    fun clearBreedData() {
        sharedPreferences.edit().remove("breed_id").apply()
    }

    fun isBreedIdSaved(): Boolean {
        return getBreedId() != -1
    }
}