package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.ClockOutRepository
import com.example.teamsuite.viewmodel.attendance.ClockOutViewModel

@Suppress("UNCHECKED_CAST")
class ClockOutViewModelFactory(private val repository: ClockOutRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClockOutViewModel(repository) as T
    }
}