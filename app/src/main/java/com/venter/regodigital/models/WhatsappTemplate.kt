package com.venter.regodigital.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WhatsappTemplateMsg(
    val id:Int,
    var hederType:String?,
    var headerPath:String?,
    var tempMsg:String?,
    val whatstemp_id:String?
):Parcelable
