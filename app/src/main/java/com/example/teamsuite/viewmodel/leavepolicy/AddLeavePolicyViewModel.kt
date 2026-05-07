package com.example.teamsuite.viewmodel.leavepolicy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyResponse
import com.example.teamsuite.data.model.leavepolicy.AddLeavePolicyWrapper
import com.example.teamsuite.repository.leavepolicy.AddLeavePolicyRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class AddLeavePolicyViewModel(private var repository: AddLeavePolicyRepository): BaseViewModel() {
    val addLeavePolicyResult = MutableLiveData<AddLeavePolicyResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun addLeavePolicy(request: AddLeavePolicyWrapper){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try{
                val response = repository.addLeavePolicy(request)
                if (response.isSuccessful && response.body() != null){
                    addLeavePolicyResult.value = response.body()
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