package com.example.teamsuite.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Department(
    val _id: String,
    val name: String
): Parcelable
