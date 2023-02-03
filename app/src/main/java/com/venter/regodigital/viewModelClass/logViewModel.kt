package com.venter.regodigital.viewModelClass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venter.regodigital.models.RegUserRes
import com.venter.regodigital.models.SignIn
import com.venter.regodigital.models.SignInReq
import com.venter.regodigital.models.SignInReqRes
import com.venter.regodigital.repository.UserRepository
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
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