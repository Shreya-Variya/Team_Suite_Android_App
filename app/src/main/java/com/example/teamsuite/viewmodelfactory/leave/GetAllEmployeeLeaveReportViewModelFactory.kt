package com.example.teamsuite.viewmodelfactory.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.leave.GetAllEmployeeLeaveReportRepository
import com.example.teamsuite.viewmodel.leave.GetAllEmployeeLeaveReportViewModel

@Suppress("UNCHECKED_CAST")
class GetAllEmployeeLeaveReportViewModelFactory(private var repository: GetAllEmployeeLeaveReportRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetAllEmployeeLeaveReportViewModel(repository) as T
    }
}