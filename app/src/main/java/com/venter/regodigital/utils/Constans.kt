package com.venter.regodigital.utils

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Constans {
    const val BASE_URL = "http://vijenter.in:7000/"
    const val WHATSAPI_URL = "https://graph.facebook.com/v15.0/"
   // const val BASE_URL ="http://192.168.42.186:9000/"
    const val TAG ="MY_TAG"


    lateinit var lan_res:Resources

    const val contact_no ="+918625905985"
    const val PREFS_CONST_FILE = "PREFS_CONST_FILE"
    const val PIN ="PIN"
    const val USER_TOKEN = "USER_TOKEN"
    const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
    const val DEVICE_TOKEN = "DEVICE_TOKEN"

    const val USER_ID = "USER_ID"
    const val SUB_DATE = "SUB_DATE"
    const val EXP_DATE = "EXP_DATE"
    const val USER_TYPE = "USER_TYPE"
    const val PLAN_ID = "PLAN_ID"


    const val GROUP_ID = "SHOP_ID"
    const val GROUP_NAME = "GROUP_NAME"
    const val USE_ACCE = "USE_ACCE"
    const val PACK_NAME = "PACK_NAME"
    const val DB_VER = "DB_VER"
    const val USER_CNT = "USER_CNT"
    const val SMS_SIGNITURE ="SMS_SIGNITURE"
    const val TEMPLATE_SETTING = "TEMPLATE_SETTING"

    //WHats Api Details
    const val FACEBOOK_KEY ="FACEBOOK_KEY"
    const val PHONENO_ID = "PHONENO_ID"
    const val ACC_STATUS = "ACC_STATUS"
    const val ACC_ID = "ACC_ID"
    const val DOC_HEAD = "DOC_HEAD"
    const val VDO_HEAD = "VDO_HEAD"
    const val IMG_HEAD = "IMG_HEAD"

    //User Previliagegs
    const val AUTO_TEMP = "AUTO_TEMP"
    const val PLAN_NAME= "PLAN_NAME"
    const val TEXT_SMS = "TEXT_SMS"
    const val WHATS_API = "WHATS_API"
    const val WHATS_VDO = "WHATS_VDO"



    val notiMutableCntData = MutableLiveData<Int> ()
    val notiCntData:LiveData<Int>
    get() = notiMutableCntData



}