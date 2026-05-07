package com.example.teamsuite.viewmodelfactory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.employee.AddEmployeeRepository
import com.example.teamsuite.viewmodel.employee.AddEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class AddEmployeeViewModelFactory(private val repository: AddEmployeeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEmployeeViewModel(repository) as T
    }
}