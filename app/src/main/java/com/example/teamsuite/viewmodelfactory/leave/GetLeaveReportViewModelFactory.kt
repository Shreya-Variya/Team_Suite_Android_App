package com.example.teamsuite.viewmodelfactory.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leave.GetLeaveReportRepository
import com.example.teamsuite.viewmodel.leave.GetLeaveReportViewModel

@Suppress("UNCHECKED_CAST")
class GetLeaveReportViewModelFactory(private var repository: GetLeaveReportRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetLeaveReportViewModel(repository) as T
    }
}