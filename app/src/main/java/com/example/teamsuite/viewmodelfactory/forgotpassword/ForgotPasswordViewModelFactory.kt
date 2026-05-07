package com.example.teamsuite.viewmodelfactory.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.forgotpassword.ForgotPasswordRepository
import com.example.teamsuite.viewmodel.forgotpassword.ForgotPasswordViewModel

@Suppress("UNCHECKED_CAST")
class ForgotPasswordViewModelFactory(private var repository: ForgotPasswordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(repository) as T
    }
}