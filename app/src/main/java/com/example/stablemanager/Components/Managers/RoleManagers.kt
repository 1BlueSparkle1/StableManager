package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class RoleManagers(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "role_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveRoleId(roleId: Int) {
        sharedPreferences.edit().putInt("role_id", roleId).apply()
    }

    fun getRoleId(): Int {
        return sharedPreferences.getInt(
            "role_id",
            -1
        )
    }

    fun clearRoleData() {
        sharedPreferences.edit().remove("role_id").apply()
    }

    fun isRoleIdSaved(): Boolean {
        return getRoleId() != -1
    }
}