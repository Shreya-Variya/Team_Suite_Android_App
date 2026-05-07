package com.example.teamsuite.viewmodelfactory.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leave.AcceptRejectLeaveRepository
import com.example.teamsuite.viewmodel.leave.AcceptRejectLeaveViewModel

@Suppress("UNCHECKED_CAST")
class AcceptRejectLeaveViewModelFactory(private var repository: AcceptRejectLeaveRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AcceptRejectLeaveViewModel(repository) as T
    }
}