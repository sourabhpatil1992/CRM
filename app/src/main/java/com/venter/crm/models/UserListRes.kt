package com.venter.crm.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserListRes(
    val id:Int,
    val user_name :String,
    val email_id:String,
    val mobile_no:String,
    val job_title :String?,
    val user_type :String,
    var status:String,
    val what_acc:Int
):Parcelable
