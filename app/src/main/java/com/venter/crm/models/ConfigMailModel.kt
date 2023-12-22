package com.venter.crm.models

import com.venter.crm.userledger.statusUser

data class ConfigMailModel(
    val id: Int = 0,
    val host: String,
    val port:Int,
    val emailId:String,
    val pass:String,
    val status:Int=0


    )