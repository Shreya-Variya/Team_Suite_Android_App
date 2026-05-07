package com.example.teamsuite.viewmodel.jobrole

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.jobrole.GetJobRoleResponse
import com.example.teamsuite.repository.jobrole.GetJobRoleRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class GetJobRoleViewModel(private var repository: GetJobRoleRepository): BaseViewModel() {
    val jobRoleData = MutableLiveData<GetJobRoleResponse>()
    val error = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun getJobRole(id: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.getJobRole(id)
                if (response.isSuccessful && response.body() != null){
                    jobRoleData.value = response.body()
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