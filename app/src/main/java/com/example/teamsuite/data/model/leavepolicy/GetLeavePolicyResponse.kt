package com.example.teamsuite.data.model.leavepolicy

data class GetLeavePolicyResponse(
    val success: Boolean,
    val message: String,
    val data: List<LeavePolicyData>
)
