package com.venter.regodigital.officeexpenses

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityExpensesReportBinding
import com.venter.regodigital.models.expensesDet
import com.venter.regodigital.utils.Constans.BASE_URL
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExpensesReportActivity : AppCompatActivity() {
    private var _binding: ActivityExpensesReportBinding? = null
    private val binding: ActivityExpensesReportBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: ExpenseReportAdapter

    private lateinit var report: List<expensesDet>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExpensesReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ExpenseReportAdapter()

        try {
            binding.btnFromDate.setOnClickListener {
                calView("FromDate")
            }
            binding.btnToDate.setOnClickListener {
                calView("ToDate")
            }

            binding.floatingPrintButton.setOnClickListener {
                print()
            }

            binding.edtTxtToDate.doOnTextChanged { text, start, before, count ->
                if (binding.edtTxtFromDate.text.isNotEmpty() && binding.edtTxtToDate.text.isNotEmpty())
                    dateCal()
            }
            binding.edtTxtFromDate.doOnTextChanged { text, start, before, count ->
                if (binding.edtTxtFromDate.text.isNotEmpty() && binding.edtTxtToDate.text.isNotEmpty())
                    dateCal()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in ExpenseReportActivity.kt is " + e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun calView(date: String) {
        try {
            val materialDateBuilder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.datePicker()

            materialDateBuilder.setTitleText("SELECT A DATE")


            val materialDatePicker = materialDateBuilder.build()


            materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

            materialDatePicker.addOnPositiveButtonClickListener {

                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

                val formatted = format.format(utc.time).toString()
                if (date == "FromDate")
                    binding.edtTxtFromDate.setText(formatted)
                else
                    binding.edtTxtToDate.setText(formatted)

            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateFeeReceiptActivity.kt calView() is " + e.message)
        }

    }

    private fun dateCal() {
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val toDate = dateFormat.parse(binding.edtTxtToDate.text.toString())
            val fromDate = dateFormat.parse(binding.edtTxtFromDate.text.toString())
            if (toDate.compareTo(fromDate) >= 0) {
                candidateViewModel.expensesReportPrint(
                    binding.edtTxtToDate.text.toString(),
                    binding.edtTxtFromDate.text.toString()
                )
                candidateViewModel.expenseReportPrintResLiveData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> Toast.makeText(
                            this,
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        is NetworkResult.Success -> {
                            report = it.data!!
                            binding.linReport.visibility = View.VISIBLE
                            binding.floatingPrintButton.visibility = View.VISIBLE
                            binding.progressbar.visibility = View.GONE
                            adapter.submitList(it.data)
                            binding.reView.layoutManager =
                                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                            binding.reView.adapter = adapter
                            // Log.d(TAG, it.data.toString())
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Please Check the differance between two dates.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in ExpeseReportActivity.kt dateCal() is " + e.message)
        }

    }

    private fun print() {
        try {
            if (report.size > 0) {


                var tableContent = ""
                var i = 1
                var credit = 0
                var debit = 0
                for (x in report) {
                    tableContent =
                        tableContent + "<tr>" +
                                "<td>" + i.toString() + "</td>" +
                                "<td>" + x.trans_date + "</td>" +
                                "<td>" + x.tras_type + "<br>" + x.trans_dscr + "</td>" +
                                " <td>" + x.category + "</td> " +
                                "<td>" + x.transAmt + "</td>"+
                                "</tr>"
                    if(x.category == "Credit")
                        credit += x.transAmt.toInt()
                    else
                        debit += x.transAmt.toInt()


                    i++
                }

                val headerstring = "<html><style>\n" +
                        "table, th, td {\n" +
                        "  border:1px solid black;\n" +
                        "}\n" +

                        "</style>" +
                        "<body background=\" " + BASE_URL + "assets/lend_logo.png\"><center><h1><b>Expense Report</b></h1></center>"


                val reportTitle =
                    "<b>Report From Date :</b>" + binding.edtTxtFromDate.text.toString() + "</t>&emsp;&emsp;&emsp;&emsp;<b>Report To Date :</b>" + binding.edtTxtToDate.text.toString()

                val item_table = "<table style=\"width:100%\">\n" +
                        "  <tr><h2>\n" +
                        "    <td><b>Sr. No. </b></td>\n" +
                        "    <td><b>Date</b></td>\n" +
                        "    <td><b>Title <br> Description</b></td>\n" +
                        "    <td><b>Type</b></td>\n" +
                        "    <td><b>Amount</b></td>\n" +
                        "  </h2></tr>\n" +
                        tableContent +
                        "</table>"



                val amount = "<table style=\"border:1px solid black;margin-left:auto;margin-right:auto;\">\n" +
                        "  <tr><h2><td><b>&emsp;Credit Amount : $credit &emsp;</b></td></h2></tr>" +
                        "  <tr><h2><td><b>&emsp;Debit Amount : $debit &emsp; </b></td></h2></tr>" +
                        "  <tr><h2><td><b>&emsp;Total Amount :  "+(credit-debit).toString()+" &emsp;</b></td></h2></tr>" +

                        "</table>"


                val closing_string = "</body> </html>"

                val data =
                    headerstring + reportTitle + item_table+amount + closing_string

                val web_view = this?.let { WebView(it) }
                web_view?.loadData(data, "text/html", "UTF-8")

                //Print Job
                (this?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

                    val jobName = "${getString(R.string.app_name)} Document"

                    // Get a print adapter instance
                    val printAdapter = web_view!!.createPrintDocumentAdapter(jobName)

                    // Create a print job with name and adapter instance
                    printManager.print(
                        jobName,
                        printAdapter,
                        PrintAttributes.Builder().build()
                    ).also { printJob ->

                        // Save the job object for later status checking
                        var printJobs = printJob


                    }


                }
            }


        } catch (e: Exception) {
            Log.d(TAG, "Error in ExpenseReportActivity.kt print() is " + e.message)
        }

    }
}