package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.BreakOutRepository
import com.example.teamsuite.viewmodel.attendance.BreakOutViewModel

@Suppress("UNCHECKED_CAST")
class BreakOutViewModelFactory(private val repository: BreakOutRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BreakOutViewModel(repository) as T
    }
}