package com.venter.crm.viewModelClass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venter.crm.models.RegUserRes
import com.venter.crm.models.SignIn
import com.venter.crm.models.SignInReq
import com.venter.crm.models.SignInReqRes
import com.venter.crm.repository.UserRepository
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class logViewModel@Inject constructor(private val userRepository: UserRepository) : ViewModel()  {
    val logInReqRes: LiveData<NetworkResult<SignInReqRes>>
        get() = userRepository.loginReqResLiveData
    fun logInReq(user: SignInReq)
    {
        try {
            viewModelScope.launch {
                userRepository.logInUser(user)
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in LogViewModel.kt logInReq() is  "+e.message)
        }
    }

    val logInResData:LiveData<NetworkResult<RegUserRes>>
        get() = userRepository.loginResLiveData
    fun logIn(user: SignIn)
    {
        try {
            viewModelScope.launch {
                userRepository.signInUser(user)
            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in LogViewModel.kt logIn() is  "+e.message)
        }
    }
}