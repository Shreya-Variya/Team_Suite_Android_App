package com.example.teamsuite.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobRole(
    val _id: String,
    val role: String
): Parcelable
