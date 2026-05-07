package com.example.teamsuite.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.login.LoginRequest
import com.example.teamsuite.data.model.login.LoginResponse
import com.example.teamsuite.repository.login.LoginRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class LoginViewModel(private val repository: LoginRepository): BaseViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val loading = MutableLiveData<Boolean>()

    fun login(email: String, password: String){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.login(
                    LoginRequest(email, password)
                )
                if (response.isSuccessful && response.body() != null){
                    _loginResult.value = response.body()
                }
                else{
                    val errMsg = parseErrorMessage(response.errorBody())
                    _error.value = errMsg
                }
            }
            catch (e: Exception){
                Log.i("ERROR", "error: ${e}")
                _error.value = "Internet Connection Failed."
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