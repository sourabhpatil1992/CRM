package com.venter.crm.models

data class EmpInfoData(
    val salutation: String,
    val fName: String,
    val mName: String,
    val lName: String,
    val mobNo: String,
    val emailId: String,
    val dept: String,
    val cPackage: Double,
    val cJob: String,
    val empId: Int
)
