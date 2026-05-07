package com.example.teamsuite.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val address: Address,
    val _id: String,
    val employeeId: String,
    val employeeName: String,
    val dob: String,
    val gender: String,
    val maritalStatus: Boolean,
    val email: String,
    val mobileNo: String,
    val education: String,
    val experience: Double,
    val department: Department,
    val jobRole: JobRole,
    val joinDate: String,
    val category: String,
    val companyId: String,
    val companyName: String,
    val __v: Int
): Parcelable
