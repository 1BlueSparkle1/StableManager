package com.example.stablemanager.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val userId = intent.getIntExtra("userId", -1)
        val isOwner = intent.getBooleanExtra("isOwner", false)
        if (userId == -1) {
            Log.e("NotificationReceiver", "User ID not found in intent")
            return
        }

        val refreshIntent = Intent("com.example.stablemanager.REFRESH_NOTIFICATION_COUNT")
        refreshIntent.putExtra("userId", userId)
        refreshIntent.putExtra("isOwner", isOwner)
        LocalBroadcastManager.getInstance(context).sendBroadcast(refreshIntent)
    }
}