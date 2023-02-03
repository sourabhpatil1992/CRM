package com.venter.regodigital.di

import com.venter.regodigital.api.AuthInterceptor
import com.venter.regodigital.api.UserApi
import com.venter.regodigital.api.UserAuthApi
import com.venter.regodigital.utils.Constans.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder() :Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)

    }

    @Singleton
    @Provides
    fun providesOkHttpclient(authInterceptor: AuthInterceptor):OkHttpClient
    {
            return  OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }


    @Singleton
    @Provides
    fun providesUserApi(retrofitBuilder: Retrofit.Builder): UserApi
    {
        return  retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserAuthApi(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient):UserAuthApi{
        return  retrofitBuilder
            .client(okHttpClient)
            .build().create(UserAuthApi::class.java)

    }


}