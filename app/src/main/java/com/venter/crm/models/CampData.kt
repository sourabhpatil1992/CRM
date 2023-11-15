package com.venter.crm.models

data class CampData(
    val campDet: String,
    val campName: String,
    val camp_id: Int,
    val dataCount: Int,
    val dataType: String,
    val interestedCount: Int,
    val notInterestedCount: Int,
    val notRespondingCount: Int,
    val paidCount: Int,
    val rawDataCount: Int
)