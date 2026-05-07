package com.example.teamsuite.viewmodelfactory.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamsuite.repository.company.GetCompanyDataRepository
import com.example.teamsuite.viewmodel.company.GetCompanyDataViewModel

@Suppress("UNCHECKED_CAST")
class GetCompanyDataViewModelFactory(private var repository: GetCompanyDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetCompanyDataViewModel(repository) as T
    }
}