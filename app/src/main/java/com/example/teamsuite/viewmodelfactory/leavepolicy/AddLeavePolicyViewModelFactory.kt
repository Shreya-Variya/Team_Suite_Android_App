package com.example.teamsuite.viewmodelfactory.leavepolicy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leavepolicy.AddLeavePolicyRepository
import com.example.teamsuite.viewmodel.leavepolicy.AddLeavePolicyViewModel

@Suppress("UNCHECKED_CAST")
class AddLeavePolicyViewModelFactory(private val repository: AddLeavePolicyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddLeavePolicyViewModel(repository) as T
    }
}