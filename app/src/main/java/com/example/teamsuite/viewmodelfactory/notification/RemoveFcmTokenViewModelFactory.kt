package com.example.teamsuite.viewmodelfactory.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.notification.RemoveFcmTokenRepository
import com.example.teamsuite.viewmodel.notification.RemoveFcmTokenViewModel

@Suppress("UNCHECKED_CAST")
class RemoveFcmTokenViewModelFactory(private var repository: RemoveFcmTokenRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RemoveFcmTokenViewModel(repository) as T
    }
}