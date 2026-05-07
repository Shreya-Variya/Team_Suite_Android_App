package com.example.teamsuite.data.model.attendance.attendancereport

data class GetEmployeeAttendanceResponse(
    val success: Boolean,
    val message: String,
    val data: List<GetEmployeeAttendanceData>
)
