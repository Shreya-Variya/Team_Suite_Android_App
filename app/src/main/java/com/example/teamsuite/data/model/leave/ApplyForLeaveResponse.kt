package com.example.teamsuite.data.model.leave

import com.example.teamsuite.data.model.login.UserData

data class ApplyForLeaveResponse(
    val success: Boolean,
    val message: String,
    val leavedata: LeaveData,
    val admindata: AdminData,
)
