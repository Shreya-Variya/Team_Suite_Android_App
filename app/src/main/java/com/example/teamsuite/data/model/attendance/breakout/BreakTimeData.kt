package com.example.teamsuite.data.model.attendance.breakout

data class BreakTimeData(
    val _id: String,
    val employeeId: String,
    val clockIn: String,
    val clockOut: String,
    val workTime: Long,
    val breakTime: Long,
    val __v: Int
)
