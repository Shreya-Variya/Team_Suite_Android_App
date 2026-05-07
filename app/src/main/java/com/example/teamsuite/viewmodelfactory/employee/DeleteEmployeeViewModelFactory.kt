package com.example.teamsuite.viewmodelfactory.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.employee.DeleteEmployeeRepository
import com.example.teamsuite.viewmodel.employee.DeleteEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class DeleteEmployeeViewModelFactory(private var repository: DeleteEmployeeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeleteEmployeeViewModel(repository) as T
    }
}