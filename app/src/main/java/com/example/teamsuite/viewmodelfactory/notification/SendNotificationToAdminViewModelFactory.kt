package com.example.teamsuite.viewmodelfactory.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.notification.SendNotificationToAdminRepository
import com.example.teamsuite.viewmodel.notification.SendNotificationToAdminViewModel

@Suppress("UNCHECKED_CAST")
class SendNotificationToAdminViewModelFactory(private var repository: SendNotificationToAdminRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SendNotificationToAdminViewModel(repository) as T
    }
}