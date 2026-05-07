package com.example.teamsuite.data.model.attendance.attendancereport

data class GetEmployeeAttendanceData(
    val employeeId: String,
    val name: String,
    val date: String,
    val status: String,
)
