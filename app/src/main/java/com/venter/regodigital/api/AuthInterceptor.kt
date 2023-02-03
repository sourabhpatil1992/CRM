package com.venter.regodigital.api

import android.content.Context
import com.venter.regodigital.utils.Constans.PREFS_CONST_FILE
import com.venter.regodigital.utils.Constans.USER_TOKEN
import com.venter.regodigital.utils.TokenManger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {

    @Inject
    lateinit var tokenManger: TokenManger
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = tokenManger.getToken()
        request.addHeader("Authorization","Bearer $token")

        return  chain.proceed(request.build())
    }
}

class AuthInterceptorWorker(context: Context) : Interceptor {

    var prefs = context.getSharedPreferences(PREFS_CONST_FILE, Context.MODE_PRIVATE)!!
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()


        val token = prefs.getString(USER_TOKEN,null)
        request.addHeader("Authorization","Bearer $token")

        return  chain.proceed(request.build())
    }
}


