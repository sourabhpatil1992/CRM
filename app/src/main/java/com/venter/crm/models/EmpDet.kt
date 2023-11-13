package com.venter.crm.models

data class EmpDet(
    val id:Int,
    val salutation:String,
    val fName:String,
    val mName:String,
    val lName:String,
    val mVisible:Int,
    val mobNo:String,
    val emailId:String,
    val gender:String,
    val dob:String,
    val address:String,
    val bank:String,
    val accNo:String,
    val pan:String,
    val bloodGrp:String,
    val empId:String,
    val dept:String,
    val job:String,
    val joinDate:String,
    val jPackage:Double,
    val appAccess:Int,
    val userType:String,
    val cJob:String,
    val cPackage:Double,
    val documets:String = ""
)
