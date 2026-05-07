package com.example.teamsuite.viewmodelfactory.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.attendance.AttendanceByDateRepository
import com.example.teamsuite.viewmodel.attendance.AttendanceByDateViewModel

@Suppress("UNCHECKED_CAST")
class AttendanceByDateViewModelFactory(private  var repository: AttendanceByDateRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AttendanceByDateViewModel(repository) as T
    }
}