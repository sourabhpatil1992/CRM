package com.venter.crm.api

import com.venter.crm.models.RegUserRes
import com.venter.crm.models.SignIn
import com.venter.crm.models.SignInReq
import com.venter.crm.models.SignInReqRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {



    @POST("/logsApi/signinreq")
    suspend fun signInRequest(@Body user: SignInReq): Response<SignInReqRes>

    @POST("/logsApi/signin")
    suspend fun signIn(@Body user: SignIn): Response<RegUserRes>




}