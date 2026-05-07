package com.example.teamsuite.viewmodelfactory.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.login.LoginRepository
import com.example.teamsuite.viewmodel.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: LoginRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}