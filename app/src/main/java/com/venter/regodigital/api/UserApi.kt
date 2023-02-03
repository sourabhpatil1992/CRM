package com.venter.regodigital.api

import com.venter.regodigital.models.RegUserRes
import com.venter.regodigital.models.SignIn
import com.venter.regodigital.models.SignInReq
import com.venter.regodigital.models.SignInReqRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {



    @POST("/logsApi/signinreq")
    suspend fun signInRequest(@Body user: SignInReq): Response<SignInReqRes>

    @POST("/logsApi/signin")
    suspend fun signIn(@Body user: SignIn): Response<RegUserRes>




}