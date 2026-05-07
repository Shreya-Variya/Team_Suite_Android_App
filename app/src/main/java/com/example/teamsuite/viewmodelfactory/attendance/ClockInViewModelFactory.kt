package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.ClockInRepository
import com.example.teamsuite.viewmodel.attendance.ClockInViewModel

@Suppress("UNCHECKED_CAST")
class ClockInViewModelFactory(private val repository: ClockInRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClockInViewModel(repository) as T
    }
}