package com.example.stablemanager.Components.Managers

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class FeedManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "feed_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveFeedId(feedId: Int) {
        sharedPreferences.edit().putInt("feed_id", feedId).apply()
    }

    fun getFeedId(): Int {
        return sharedPreferences.getInt(
            "feed_id",
            -1
        )
    }

    fun clearFeedData() {
        sharedPreferences.edit().remove("feed_id").apply()
    }

    fun isFeedIdSaved(): Boolean {
        return getFeedId() != -1
    }
}