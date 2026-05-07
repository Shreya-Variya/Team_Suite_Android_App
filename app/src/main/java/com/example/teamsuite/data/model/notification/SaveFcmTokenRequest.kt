package com.example.teamsuite.data.model.notification

data class SaveFcmTokenRequest(
    val employeeId: String,
    val category: String,
    val fcmToken: String,
)
