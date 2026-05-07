package com.example.teamsuite.data.model.attendance.attendancereport

data class AttendanceByDateData(
    val employeeId: String,
    val name: String,
    val date: String,
    val status: String,
    val workTime: Long,
    val breakTime: Long,
)
