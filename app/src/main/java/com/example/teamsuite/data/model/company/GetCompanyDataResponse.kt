package com.example.teamsuite.data.model.company

data class GetCompanyDataResponse(
    val success: Boolean,
    val message: String,
    val data: CompanyData,
)
