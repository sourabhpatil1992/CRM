package com.venter.regodigital.models

data class CandidateFeeList(
    val id: Int,
    val first_name: String,
    val middel_name: String,
    val last_name: String,
    val cource_fee: Double?,
    val paidFee: Double?,
    val transReq:String
)
