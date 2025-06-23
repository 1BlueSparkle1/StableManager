package com.example.stablemanager.db

class Notification(val id: Int, val description: String, val isRead: Boolean, val employeeId: Int?, val ownerId: Int?, val creationDate: String, val senderEmployeeId: Int?, val senderOwnerId: Int?) {
}