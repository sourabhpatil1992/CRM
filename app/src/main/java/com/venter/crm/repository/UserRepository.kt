package com.venter.crm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.venter.crm.api.UserApi
import com.venter.crm.models.RegUserRes
import com.venter.crm.models.SignIn
import com.venter.crm.models.SignInReq
import com.venter.crm.models.SignInReqRes
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi)
{

    private val _loginReqResLiveData = MutableLiveData<NetworkResult<SignInReqRes>>()
    val loginReqResLiveData: LiveData<NetworkResult<SignInReqRes>>
        get() = _loginReqResLiveData

    suspend fun logInUser(user: SignInReq) {
        try {

            _loginReqResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.signInRequest(user)

            if (response.isSuccessful && response.body() != null) {

                _loginReqResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _loginReqResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _loginReqResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in UserRepository.kt logInUser() is " + e.message)
        }
    }


    private val _loginResLiveData = MutableLiveData<NetworkResult<RegUserRes>>()
    val loginResLiveData: LiveData<NetworkResult<RegUserRes>>
        get() = _loginResLiveData
    suspend fun signInUser(user: SignIn)
    {
        try {
            _loginResLiveData.postValue(NetworkResult.Loading())

            val response = userApi.signIn(user)

            if (response.isSuccessful && response.body() != null) {

                _loginResLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {


                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())

                _loginResLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {

                _loginResLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in UserRepository.kt signInUser() is " + e.message)
        }
    }

}