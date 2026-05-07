package com.example.teamsuite.viewmodelfactory.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leave.ApplyForLeaveRepository
import com.example.teamsuite.viewmodel.leave.ApplyForLeaveViewModel

@Suppress("UNCHECKED_CAST")
class ApplyForLeaveViewModelFactory(private var repository: ApplyForLeaveRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApplyForLeaveViewModel(repository) as T
    }
}