package com.venter.regodigital.models

data class RawDataList(
    val id:Int,
    val candidate_name:String,
    val mob_no:String,
    val prospect_type:String?,
    val SourceOfApplication:String,
    var selected:Boolean = false

)
