package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.BreakInRepository
import com.example.teamsuite.viewmodel.attendance.BreakInViewModel

@Suppress("UNCHECKED_CAST")
class BreakInViewModelFactory(private val repository: BreakInRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BreakInViewModel(repository) as T
    }
}