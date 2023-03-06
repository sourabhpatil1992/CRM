package com.venter.regodigital.models

data class RawCandidateData(
    val candidate_name:String,
    val emailId:String,
    var mob_no:String,
    val curr_location:String,
    val SourceOfApplication:String,
    val appDate:String,
    val Qualification:String,
    val PassYear:String,
    val commentList:List<RawDataComment>
)

data class RawDataComment(
    val commentDate:String,
    val remark: String,
    val follloupDate: String,
    val created_by: Int,
    val empName: String
)

