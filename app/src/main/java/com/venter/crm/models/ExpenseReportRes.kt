package com.venter.crm.models

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

data class  expensesDet(
    val trans_id:String,
    val trans_date:String,
    val tras_type:String,
    val trans_dscr:String,
    val category:String,
    val transAmt:Double
)
