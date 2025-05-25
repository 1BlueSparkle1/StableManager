package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TypeBreedManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "type_breed_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTypeBreedId(typeBreedId: Int) {
        sharedPreferences.edit().putInt("type_breed_id", typeBreedId).apply()
    }

    fun getTypeBreedId(): Int {
        return sharedPreferences.getInt(
            "type_breed_id",
            -1
        )
    }

    fun clearTypeBreedData() {
        sharedPreferences.edit().remove("type_breed_id").apply()
    }

    fun isTypeBreedIdSaved(): Boolean {
        return getTypeBreedId() != -1
    }
}