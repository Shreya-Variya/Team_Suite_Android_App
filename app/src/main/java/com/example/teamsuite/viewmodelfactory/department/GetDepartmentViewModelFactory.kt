package com.example.teamsuite.viewmodelfactory.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.department.GetDepartmentRepository
import com.example.teamsuite.viewmodel.department.GetDepartmentViewModel

@Suppress("UNCHECKED_CAST")
class GetDepartmentViewModelFactory(private var repository: GetDepartmentRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetDepartmentViewModel(repository) as T
    }
}