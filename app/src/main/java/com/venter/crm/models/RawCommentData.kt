package com.venter.crm.models

data class RawCommentData(
    val callTime: String,
    val prosType: String,
    val remark: String,
    val folloupDate: String,
    val selectedItem: ArrayList<Int>,
    val prosUpdate: Int, val mobNo: String, val alterMobNo: String, val prosLevel: String, val amount: Int,val candidateId:Int
)
