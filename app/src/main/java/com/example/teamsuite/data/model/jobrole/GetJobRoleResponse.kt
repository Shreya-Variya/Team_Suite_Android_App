package com.example.teamsuite.data.model.jobrole

data class GetJobRoleResponse(
    val success: Boolean,
    val message: String,
    val data: List<JobRoleData>
)
