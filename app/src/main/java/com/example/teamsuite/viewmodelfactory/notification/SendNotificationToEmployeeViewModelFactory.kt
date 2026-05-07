package com.example.teamsuite.viewmodelfactory.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.notification.SendNotificationToEmployeeRepository
import com.example.teamsuite.viewmodel.notification.SendNotificationToEmployeeViewModel

@Suppress("UNCHECKED_CAST")
class SendNotificationToEmployeeViewModelFactory(private var repository: SendNotificationToEmployeeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SendNotificationToEmployeeViewModel(repository) as T
    }
}