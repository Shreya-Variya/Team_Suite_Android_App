package com.example.teamsuite.viewmodel.employee

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.data.model.employee.GetSpecificEmployeeResponse
import com.example.teamsuite.repository.employee.GetSpecificEmployeeRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class GetSpecificEmployeeViewModel(private val repository: GetSpecificEmployeeRepository): ViewModel() {
    val empData = MutableLiveData<GetSpecificEmployeeResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun getSpecificEmployee(email: String){
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repository.getSpecificEmployee(email)
                if (response.isSuccessful && response.body() != null){
                    empData.value = response.body()
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