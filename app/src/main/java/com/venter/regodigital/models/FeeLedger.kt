package com.venter.regodigital.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeeLedger(
    val id:Int,
    val trans_id:Int,
    val date:String,
    val amt:Double,
    val remark:String
): Parcelable

@Parcelize
data class FeeLedgerDet(
    val feeLedger : List<FeeLedger>,
    val nextPaymentDate:String
): Parcelable
