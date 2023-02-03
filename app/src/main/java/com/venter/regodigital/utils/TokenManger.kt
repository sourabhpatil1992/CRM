package com.venter.regodigital.utils

import android.content.Context
import android.util.Log

import com.venter.regodigital.utils.Constans.DEVICE_TOKEN
import com.venter.regodigital.utils.Constans.FIREBASE_TOKEN
import com.venter.regodigital.utils.Constans.PREFS_CONST_FILE
import com.venter.regodigital.utils.Constans.USER_ID
import com.venter.regodigital.utils.Constans.USER_TOKEN
import com.venter.regodigital.utils.Constans.USER_TYPE

import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class TokenManger @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_CONST_FILE, Context.MODE_PRIVATE)


    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveFirebaseToken(token: String) {
        val editor = prefs.edit()
        editor.putString(FIREBASE_TOKEN, token)
        editor.apply()
    }

    fun getFirebaseToken(): String? {
        return prefs.getString(FIREBASE_TOKEN, null)
    }

    fun saveDevToken() {
        // val currentTime = SimpleDateFormat("DDMMYYYYHHmmssSSS", Locale.getDefault()).format(Date())
        val token =
            android.os.Build.BRAND + android.os.Build.MODEL + android.os.Build.ID//+currentTime
        val editor = prefs.edit()
        editor.putString(DEVICE_TOKEN, token.replace("\\s".toRegex(), ""))
        editor.apply()
    }

    fun getDevToken(): String? {
        return prefs.getString(DEVICE_TOKEN, null)
    }

    fun saveUserDet(user:String,userId:String){
        val editor = prefs.edit()
        editor.putString(USER_TYPE, user)
        editor.putString(USER_ID , userId)
        editor.apply()
    }

    fun getUserType(): String? {
        return prefs.getString(USER_TYPE, null)
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }




}