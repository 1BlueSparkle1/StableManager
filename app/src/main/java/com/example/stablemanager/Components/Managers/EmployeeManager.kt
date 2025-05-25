package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EmployeeManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "employee_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveEmployeeId(employeeId: Int) {
        sharedPreferences.edit().putInt("employee_id", employeeId).apply()
    }

    fun getEmployeeId(): Int {
        return sharedPreferences.getInt(
            "employee_id",
            -1
        )
    }

    fun clearEmployeeData() {
        sharedPreferences.edit().remove("employee_id").apply()
    }

    fun isEmployeeIdSaved(): Boolean {
        return getEmployeeId() != -1
    }
}