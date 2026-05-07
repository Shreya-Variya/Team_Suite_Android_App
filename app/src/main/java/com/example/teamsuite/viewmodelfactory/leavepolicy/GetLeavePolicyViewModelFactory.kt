package com.example.teamsuite.viewmodelfactory.leavepolicy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leavepolicy.GetLeavePolicyRepository
import com.example.teamsuite.viewmodel.leavepolicy.GetLeavePolicyViewModel

@Suppress("UNCHECKED_CAST")
class GetLeavePolicyViewModelFactory(private var repository: GetLeavePolicyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetLeavePolicyViewModel(repository) as T
    }
}