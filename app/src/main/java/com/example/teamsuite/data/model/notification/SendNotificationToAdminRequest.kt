package com.example.teamsuite.data.model.notification

data class SendNotificationToAdminRequest(
    val adminId: String,
    val leaveId: String,
    val employeeId: String,
    val title: String,
    val body: String,
    val type: String,
)
