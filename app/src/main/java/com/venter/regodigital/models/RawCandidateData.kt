package com.venter.regodigital.models

data class RawCandidateData(
    val candidate_name:String,
    val emailId:String,
    var mob_no:String,
    var altenate_mobno:String?,
    val curr_location:String,
    val SourceOfApplication:String,
    val appDate:String,
    val Qualification:String,
    val PassYear:String,
    val prospect_type:String,
    val commentList:List<RawDataComment>
)

data class RawDataComment(
    val commentId:Int,
    val commentDate:String,
    val remark: String,
    val follloupDate: String,
    val created_by: Int,
    val empName: String
)

