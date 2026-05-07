package com.example.teamsuite.data.model.notification

data class SendNotificationToEmployeeRequest(
    val employeeId: String,
    val title: String,
    val body: String,
    val type: String,
)
