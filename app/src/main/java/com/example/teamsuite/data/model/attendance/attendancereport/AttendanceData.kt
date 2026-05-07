package com.example.teamsuite.data.model.attendance.attendancereport

data class AttendanceData(
    val date: String,
    val status: String,
    val workTime: Long,
    val breakTime: Long
)
