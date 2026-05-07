package com.example.teamsuite.viewmodelfactory.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.stats.AdminStatsRepository
import com.example.teamsuite.viewmodel.stats.AdminStatsViewModel

@Suppress("UNCHECKED_CAST")
class AdminStatsViewModelFactory(private var repository: AdminStatsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminStatsViewModel(repository) as T
    }
}