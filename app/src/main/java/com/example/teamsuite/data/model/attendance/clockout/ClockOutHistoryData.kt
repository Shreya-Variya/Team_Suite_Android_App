package com.example.teamsuite.data.model.attendance.clockout

data class ClockOutHistoryData(
    val _id: String,
    val attendanceId: String,
    val clockIn: String,
    val type: String,
    val __v: Int,
    val clockOut: String
)
