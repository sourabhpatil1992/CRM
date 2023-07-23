package com.venter.regodigital.models

data class RawDataList(
    val id:Int,
    var srNo:Int=0,
    val candidate_name:String,
    val mob_no:String,
    val prospect_type:String?,
    val SourceOfApplication:String,
    var selected:Boolean = false,
    var update_on:String,
    val prosLevel:String

)
