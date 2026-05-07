package com.example.teamsuite.data.model.adminstats

data class GetTotalEmployeesResponse(
    val success: Boolean,
    val message: String,
    val totalEmployees: Int,
    val totalPresentEmployees: Int,
    val totalAbsentEmployees: Int,
)
