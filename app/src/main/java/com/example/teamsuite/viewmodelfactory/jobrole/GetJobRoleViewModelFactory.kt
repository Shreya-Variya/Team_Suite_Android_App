package com.example.teamsuite.viewmodelfactory.jobrole

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.jobrole.GetJobRoleRepository
import com.example.teamsuite.viewmodel.jobrole.GetJobRoleViewModel

@Suppress("UNCHECKED_CAST")
class GetJobRoleViewModelFactory(private var repository: GetJobRoleRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetJobRoleViewModel(repository) as T
    }
}