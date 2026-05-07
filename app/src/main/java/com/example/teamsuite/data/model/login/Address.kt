package com.example.teamsuite.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val street: String,
    val city: String,
    val state: String
): Parcelable
