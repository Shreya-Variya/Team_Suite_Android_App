package com.example.teamsuite.data.model.employeestats

data class GetCurrentMonthAttendanceResponse(
    val success: Boolean,
    val message: String,
    val present: Int,
    val absent: Int,
)
