package com.example.teamsuite.abstractclass

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    fun setLoading(value: Boolean){
        isLoading.postValue(value)
    }
}