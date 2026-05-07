package com.example.teamsuite.viewmodelfactory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.employee.GetAllEmployeesRepository
import com.example.teamsuite.viewmodel.employee.GetAllEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class GetAllEmployeeViewModelFactory(private val repository: GetAllEmployeesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetAllEmployeeViewModel(repository) as T
    }
}