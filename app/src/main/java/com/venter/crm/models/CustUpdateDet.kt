package com.venter.crm.models

data class CustUpdateDet(
    val candidateId:Int,
    val candidate_name:String,
    var altenate_mobno:String?,
    val emailId:String,
    val capital: Double = 0.0,
    val trader: Int = 0,
    val segment: ArrayList<String>
)