package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.AttendanceReportRepository
import com.example.teamsuite.viewmodel.attendance.AttendanceReportViewModel

@Suppress("UNCHECKED_CAST")
class AttendanceReportViewModelFactory(private val repository: AttendanceReportRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AttendanceReportViewModel(repository) as T
    }
}