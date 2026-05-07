package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.GetEmployeeAttendanceRepository
import com.example.teamsuite.viewmodel.attendance.GetEmployeeAttendanceViewModel

@Suppress("UNCHECKED_CAST")
class GetEmployeeAttendanceViewModelFactory(private val repository: GetEmployeeAttendanceRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetEmployeeAttendanceViewModel(repository) as T
    }
}