package com.example.teamsuite.viewmodelfactory.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.changepassword.ChangePasswordRepository
import com.example.teamsuite.viewmodel.changepassword.ChangePasswordViewModel

@Suppress("UNCHECKED_CAST")
class ChangePasswordViewModelFactory(private val repository: ChangePasswordRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChangePasswordViewModel(repository) as T
    }
}