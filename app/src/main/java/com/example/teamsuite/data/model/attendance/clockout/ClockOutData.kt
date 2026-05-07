package com.example.teamsuite.data.model.attendance.clockout

data class ClockOutData(
    val _id: String,
    val employeeId: String,
    val clockIn: String,
    val clockOut: String,
    val workTime: Int,
    val breakTime: Int,
    val __v: Int
)
