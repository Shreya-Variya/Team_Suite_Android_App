package com.example.teamsuite.viewmodelfactory.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.stats.EmployeeStatsRepository
import com.example.teamsuite.viewmodel.stats.EmployeeStatsViewModel

@Suppress("UNCHECKED_CAST")
class EmployeeStatsViewModelFactory(private var repository: EmployeeStatsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmployeeStatsViewModel(repository) as T
    }
}