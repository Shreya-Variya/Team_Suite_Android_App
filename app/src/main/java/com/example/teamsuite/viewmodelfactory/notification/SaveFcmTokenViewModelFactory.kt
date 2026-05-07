package com.example.teamsuite.viewmodelfactory.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.notification.SaveFcmTokenRepository
import com.example.teamsuite.viewmodel.notification.SaveFcmTokenViewModel

@Suppress("UNCHECKED_CAST")
class SaveFcmTokenViewModelFactory(private var repository: SaveFcmTokenRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveFcmTokenViewModel(repository) as T
    }
}