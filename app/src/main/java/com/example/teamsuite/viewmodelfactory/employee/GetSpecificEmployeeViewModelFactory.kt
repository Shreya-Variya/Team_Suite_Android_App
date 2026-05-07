package com.example.teamsuite.viewmodelfactory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.employee.GetSpecificEmployeeRepository
import com.example.teamsuite.viewmodel.employee.GetSpecificEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class GetSpecificEmployeeViewModelFactory(private val repository: GetSpecificEmployeeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetSpecificEmployeeViewModel(repository) as T
    }
}