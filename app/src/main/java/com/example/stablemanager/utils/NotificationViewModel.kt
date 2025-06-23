package com.example.stablemanager.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stablemanager.db.DBHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val dbHelper: DBHelper, private val currentUserId: Int, private val isCurrentUserOwner: Boolean) : ViewModel() {

    private val _unreadNotificationsCount = MutableStateFlow(0)
    val unreadNotificationsCount: StateFlow<Int> = _unreadNotificationsCount.asStateFlow()

    init {
        refreshNotificationsCount()
    }

    fun refreshNotificationsCount() {
        viewModelScope.launch {
            val count = dbHelper.getUnreadNotificationsCount(currentUserId, isCurrentUserOwner)
            _unreadNotificationsCount.value = count
        }
    }
}

class NotificationViewModelFactory(
    private val dbHelper: DBHelper,
    private val userId: Int,
    private val isOwner: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(dbHelper, userId, isOwner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}