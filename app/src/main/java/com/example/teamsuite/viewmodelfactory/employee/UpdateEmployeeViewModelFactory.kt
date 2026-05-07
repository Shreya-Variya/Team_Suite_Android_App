package com.example.teamsuite.viewmodelfactory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.employee.UpdateEmployeeRepository
import com.example.teamsuite.viewmodel.employee.UpdateEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class UpdateEmployeeViewModelFactory(private val repository: UpdateEmployeeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateEmployeeViewModel(repository) as T
    }
}