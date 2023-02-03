package com.venter.regodigital.models

data class ExpenseReportRes(
    val totalReportDebit:List<expenses?>,
    val monthReportDebit:List<expenses?>,
    val totalReportCredit:List<expenses?>,
    val monthReportCredit:List<expenses?>
)

data class  expenses(
val tras_type:String,
val amt:Double
)
