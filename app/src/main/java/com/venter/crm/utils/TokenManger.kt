package com.venter.crm.utils

import android.content.Context
import com.venter.crm.utils.Constans.CALL_TIME

import com.venter.crm.utils.Constans.DEVICE_TOKEN
import com.venter.crm.utils.Constans.FIREBASE_TOKEN
import com.venter.crm.utils.Constans.INSTITUTE_ID
import com.venter.crm.utils.Constans.LAST_MOB
import com.venter.crm.utils.Constans.PREFS_CONST_FILE
import com.venter.crm.utils.Constans.USER_ID
import com.venter.crm.utils.Constans.USER_TOKEN
import com.venter.crm.utils.Constans.USER_TYPE

import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
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

    fun saveUserDet(user: String, userId: String, instituteId: Int){
        val editor = prefs.edit()
        editor.putString(USER_TYPE, user)
        editor.putString(USER_ID , userId)
        editor.putInt(INSTITUTE_ID , instituteId)
        editor.apply()
    }

    fun getInstituteId():Int{
        return prefs.getInt(INSTITUTE_ID, 0)
    }

    fun getUserType(): String? {
        return prefs.getString(USER_TYPE, null)
    }

    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun saveLastCommentDet(mob_no:String,time:String){
        val editor = prefs.edit()
        editor.putString(LAST_MOB, mob_no)
        editor.putString(CALL_TIME , time)
        editor.apply()
    }

    fun getLastCommentDet(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("mob_no" ,prefs.getString(LAST_MOB, ""))
        jsonObject.put("call_time" ,prefs.getString(CALL_TIME, "0"))
        return  jsonObject

    }




}