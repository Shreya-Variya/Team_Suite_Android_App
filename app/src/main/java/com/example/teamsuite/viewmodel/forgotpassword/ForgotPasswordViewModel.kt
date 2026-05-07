package com.example.teamsuite.viewmodel.forgotpassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.teamsuite.abstractclass.BaseViewModel
import com.example.teamsuite.data.model.forgotpassword.ForgotPasswordResponse
import com.example.teamsuite.data.model.forgotpassword.ResetPasswordRequest
import com.example.teamsuite.data.model.forgotpassword.SendCodeRequest
import com.example.teamsuite.data.model.forgotpassword.VerifyCodeRequest
import com.example.teamsuite.repository.forgotpassword.ForgotPasswordRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ForgotPasswordViewModel(private var repository: ForgotPasswordRepository): BaseViewModel() {
    private val _sendCodeResult = MutableLiveData<ForgotPasswordResponse?>()
    val sendCodeResult: LiveData<ForgotPasswordResponse?> = _sendCodeResult

    private val _verifyCodeResult = MutableLiveData<ForgotPasswordResponse?>()
    val verifyCodeResult: LiveData<ForgotPasswordResponse?> = _verifyCodeResult

    private val _resetPasswordResult = MutableLiveData<ForgotPasswordResponse?>()
    val resetPasswordResult: LiveData<ForgotPasswordResponse?> = _resetPasswordResult
//    val sendCodeResult = MutableLiveData<ForgotPasswordResponse>()
//    val verifyCodeResult = MutableLiveData<ForgotPasswordResponse>()
    val error = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun sendcode(request: SendCodeRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.sendcode(request)
                if (response.isSuccessful && response.body() != null){
                    _sendCodeResult.value = response.body()
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

    fun verifyCode(request: VerifyCodeRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.verifyCode(request)
                if (response.isSuccessful && response.body() != null){
                    _verifyCodeResult.value = response.body()
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

    fun resetPassword(request: ResetPasswordRequest){
        viewModelScope.launch {
            setLoading(true)
            loading.value = true
            try {
                val response = repository.resetPassword(request)
                if (response.isSuccessful && response.body() != null){
                    _resetPasswordResult.value = response.body()
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

    fun clearSendCodeResult() {
        _sendCodeResult.value = null
    }

    fun clearVerifyCodeResult() {
        _verifyCodeResult.value = null
    }

    fun clearResetPasswordResult() {
        _resetPasswordResult.value = null
    }

    fun clearError() {
        error.value = null
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