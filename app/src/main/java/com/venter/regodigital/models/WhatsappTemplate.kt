package com.venter.regodigital.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WhatsappTemplateMsg(
    val id:Int,
    var tempMsg:String?,
    val header_name:String?
):Parcelable
