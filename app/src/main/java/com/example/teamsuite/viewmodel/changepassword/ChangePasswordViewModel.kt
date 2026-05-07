package com.example.teamsuite.viewmodel.changepassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.changepassword.ChangePasswordRequest
import com.example.teamsuite.data.model.changepassword.ChangePasswordResponse
import com.example.teamsuite.repository.changepassword.ChangePasswordRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ChangePasswordViewModel(private var repository: ChangePasswordRepository): BaseViewModel() {
    private val _changePasswordResult = MutableLiveData<ChangePasswordResponse>()
    val changePasswordResult: LiveData<ChangePasswordResponse> = _changePasswordResult
    val error = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun changePassword(request: ChangePasswordRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.changePassword(request)
                if (response.isSuccessful && response.body() != null){
                    _changePasswordResult.value = response.body()
                }
                else{
                    val errMsg = parseErrorMessage(response.errorBody())
                    error.value = errMsg
                }
            }
            catch (e: Exception){
                Log.i("ERROR", "error: ${e}")
                error.value = "Internet Connection Failed."
            }
            setLoading(false)
            loading.value = false
        }
    }

    private fun parseErrorMessage(errorBody: ResponseBody?): String {
        return try{
            errorBody?.string()?.let { errorString ->
                val json = JSONObject(errorString)
                json.optString("message","Unknown error")
            }?: "Unknown error"
        }
        catch (e: Exception){
            "Unknown error"
        }
    }
}