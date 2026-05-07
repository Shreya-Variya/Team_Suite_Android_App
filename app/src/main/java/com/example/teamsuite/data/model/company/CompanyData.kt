package com.example.teamsuite.data.model.company

import com.example.teamsuite.data.model.login.Address

data class CompanyData(
    val logo: Logo,
    val address: Address,
    val workingDay: WorkingDays,
    val _id: String,
    val companyName: String,
    val domain: String,
    val website: String,
    val email: String,
    val about: String,
    val startTime: String,
    val endTime: String,
    val __v: Int,
)
