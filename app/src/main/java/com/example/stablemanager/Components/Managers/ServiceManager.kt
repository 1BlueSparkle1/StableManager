package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class ServiceManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "service_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveServiceId(serviceId: Int) {
        sharedPreferences.edit().putInt("service_id", serviceId).apply()
    }

    fun getServiceId(): Int {
        return sharedPreferences.getInt(
            "service_id",
            -1
        )
    }

    fun clearServiceData() {
        sharedPreferences.edit().remove("service_id").apply()
    }

    fun isServiceIdSaved(): Boolean {
        return getServiceId() != -1
    }
}