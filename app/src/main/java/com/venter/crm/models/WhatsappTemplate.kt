package com.venter.crm.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WhatsappTemplateMsg(
    val id:Int,
    var srNo:Int=0,
    val temp_name:String?,
    var tempMsg:String?,
    var header_name:String?
):Parcelable
