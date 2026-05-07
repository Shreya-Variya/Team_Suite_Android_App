package com.example.teamsuite.data.model.department

data class GetDepartmentResponse(
    val success: Boolean,
    val message: String,
    val data: List<DepartmentData>
)
