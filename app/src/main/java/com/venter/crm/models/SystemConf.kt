package com.venter.crm.models

data class SystemConf(
    val commentTemp:String?=null,
    val prosSubType: List<ProsSubType>? =null,
    val prosType: String? =null,

)
data class ProsSubType(
    val subType:String,
    val color:String
)

data class CommentConf(
    val commentTemp:String?=null,
    val prosSubType: List<ProsSubType>? =null,
    val prosType: String? =null,
    val whatsTemp :List<WhatsTempNameList>? = null

    )

