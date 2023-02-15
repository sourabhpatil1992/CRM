package com.venter.regodigital.officeexpenses

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityExpensesDashboardBinding
import com.venter.regodigital.models.ExpenseReportRes
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExpensesDashboardActivity : AppCompatActivity() {
    private var _binding: ActivityExpensesDashboardBinding? = null
    private val binding: ActivityExpensesDashboardBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExpensesDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        candidateViewModel.expensesReport()

        candidateViewModel.expenseReportResLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    .show()
                is NetworkResult.Success -> setView(it.data!!)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, ExpenseRecptActivity::class.java)
            startActivity(intent)
        }
        binding.floatingPrintButton.setOnClickListener {
            val intent = Intent(this, ExpensesReportActivity::class.java)
            startActivity(intent)
            /*val builders = AlertDialog.Builder(this)
            builders.setTitle("Transaction Report")
            builders.setMessage("Print Transaction Report of :")
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val fromDateText = TextView(this)
            fromDateText.setTextColor(resources.getColor(R.color.black))
            fromDateText.setText("From Date : ")
            val fromDate = EditText(this)
            fromDate.setHint("From Date")
            fromDate.filters = arrayOf(InputFilter.LengthFilter(10))

            val toDateText = TextView(this)
            toDateText.setTextColor(resources.getColor(R.color.black))
            toDateText.setText("To Date : ")
            val toDate = EditText(this)
            toDate.setHint("To Date")
            toDate.filters = arrayOf(InputFilter.LengthFilter(10))

            layout.setPadding(40, 5, 40, 0);
            layout.addView(fromDateText)
            layout.addView(fromDate)
            layout.addView(toDateText)
            layout.addView(toDate)


            fromDate.setOnClickListener {
                calView(fromDate)
            }
            toDate.setOnClickListener {
                calView(toDate)
            }

            builders.setView(layout)

            builders.setPositiveButton("Print Report") { dialogInterface, which ->


            }

            builders.setIcon(ContextCompat.getDrawable(this, R.drawable.regologo))
            val alertDialog: AlertDialog = builders.create()
            alertDialog.setCancelable(true)
            alertDialog.show()*/
        }
    }

    private fun calView(fromDate: EditText) {
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("SELECT A DATE")


        val materialDatePicker = materialDateBuilder.build()


        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            fromDate.setText(format.format(utc.time).toString())
        }

    }

    private fun setView(data: ExpenseReportRes) {
        try {
            val colorArray = arrayOf(
                applicationContext.getColor(R.color.temp1),
                applicationContext.getColor(R.color.temp2),
                applicationContext.getColor(R.color.temp3),
                applicationContext.getColor(R.color.temp4),
                applicationContext.getColor(R.color.temp5),
                applicationContext.getColor(R.color.temp6),
                applicationContext.getColor(R.color.temp7),
                applicationContext.getColor(R.color.temp8),
                applicationContext.getColor(R.color.temp9),
                applicationContext.getColor(R.color.temp10)
            )

            if (data.totalReportDebit!!.size > 0) {
                binding.linTotalReport.visibility = View.VISIBLE
                val totalReport = data.totalReportDebit
                var sum = 0.0
                totalReport.forEach {

                        sum += it!!.amt
                }
                binding.txtTotalExpenses.setText(sum.toString()+"/-")
                for (i in totalReport) {

                        binding.piechartTotal.addPieSlice(
                            PieModel(
                                i!!.tras_type,
                                (i.amt * 100 / sum).toFloat(),
                                colorArray[totalReport.indexOf(i)]
                            )
                        )
                        //val linView = LinearLayout(this)
                        val layout = LinearLayout(this)
                        layout.orientation = LinearLayout.HORIZONTAL
                        layout.setPadding(10, 5, 0, 0);
                        layout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val view = View(this)
                        view.setBackgroundColor(colorArray[totalReport.indexOf(i)])
                        view.layoutParams = ViewGroup.LayoutParams(50, 50)
                        layout.addView(view)
                        val text = TextView(this)
                        text.setText(i!!.tras_type)
                        text.setTextColor(resources.getColor(R.color.black))
                        text.setPadding(10, 0, 0, 0);
                        layout.addView(text)
                        //text.layoutParams = LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)

                        binding.linViewTotalRport.addView(layout)
                    }

            }
            if (data.monthReportDebit!!.size > 0) {
                binding.linMonthReport.visibility = View.VISIBLE
                val totalReport = data.monthReportDebit
                var sum = 0.0
                totalReport.forEach {
                        sum += it!!.amt
                }
                binding.txtMonthExpenses.setText(sum.toString()+"/-")
                for (i in totalReport) {

                        binding.piechartMonth.addPieSlice(
                            PieModel(
                                i!!.tras_type,
                                (i.amt * 100 / sum).toFloat(),
                                colorArray[totalReport.indexOf(i)]
                            )
                        )
                        //val linView = LinearLayout(this)
                        val layout = LinearLayout(this)
                        layout.orientation = LinearLayout.HORIZONTAL
                        layout.setPadding(10, 5, 0, 0);
                        layout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val view = View(this)
                        view.setBackgroundColor(colorArray[totalReport.indexOf(i)])
                        view.layoutParams = ViewGroup.LayoutParams(50, 50)
                        layout.addView(view)
                        val text = TextView(this)
                        text.setText(i.tras_type)
                        text.setTextColor(resources.getColor(R.color.black))
                        text.setPadding(10, 0, 0, 0);
                        layout.addView(text)
                        //text.layoutParams = LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)

                        binding.linViewMonthRport.addView(layout)
                    }

            }

            if (data.totalReportCredit!!.size > 0) {
                binding.linTotalIncomeReport.visibility = View.VISIBLE
                val totalReport = data.totalReportCredit
                var sum = 0.0
                totalReport.forEach {

                    sum += it!!.amt
                }
                binding.txtTotalIcome.setText(sum.toString()+"/-")
                for (i in totalReport) {

                    binding.piechartTotalincome.addPieSlice(
                        PieModel(
                            i!!.tras_type,
                            (i.amt * 100 / sum).toFloat(),
                            colorArray[totalReport.indexOf(i)]
                        )
                    )
                    //val linView = LinearLayout(this)
                    val layout = LinearLayout(this)
                    layout.orientation = LinearLayout.HORIZONTAL
                    layout.setPadding(10, 5, 0, 0);
                    layout.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val view = View(this)
                    view.setBackgroundColor(colorArray[totalReport.indexOf(i)])
                    view.layoutParams = ViewGroup.LayoutParams(50, 50)
                    layout.addView(view)
                    val text = TextView(this)
                    text.setText(i.tras_type)
                    text.setTextColor(resources.getColor(R.color.black))
                    text.setPadding(10, 0, 0, 0);
                    layout.addView(text)
                    //text.layoutParams = LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)

                    binding.linViewTotalRportIncome.addView(layout)
                }

            }
            if (data.monthReportCredit!!.size > 0) {
                binding.linMonthlyIncomeReport.visibility = View.VISIBLE
                val totalReport = data.monthReportCredit
                var sum = 0.0
                totalReport.forEach {
                    sum += it!!.amt
                }
                binding.txtMonthIcome.setText(sum.toString()+"/-")

                for (i in totalReport) {

                    binding.piechartMonthincome.addPieSlice(
                        PieModel(
                            i!!.tras_type,
                            (i.amt * 100 / sum).toFloat(),
                            colorArray[totalReport.indexOf(i)]
                        )
                    )
                    //val linView = LinearLayout(this)
                    val layout = LinearLayout(this)
                    layout.orientation = LinearLayout.HORIZONTAL
                    layout.setPadding(10, 5, 0, 0);
                    layout.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val view = View(this)
                    view.setBackgroundColor(colorArray[totalReport.indexOf(i)])
                    view.layoutParams = ViewGroup.LayoutParams(50, 50)
                    layout.addView(view)
                    val text = TextView(this)
                    text.setText(i.tras_type)
                    text.setTextColor(resources.getColor(R.color.black))
                    text.setPadding(10, 0, 0, 0);
                    layout.addView(text)
                    //text.layoutParams = LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)

                    binding.linViewMonthRportIncome.addView(layout)
                }

            }
            binding.piechartMonth.startAnimation()
            binding.piechartTotal.startAnimation()
            binding.piechartMonthincome.startAnimation()
            binding.piechartTotalincome.startAnimation()

        } catch (e: Exception) {
            Log.d(TAG, "Error in ExpenseDashboardActivity.kt setView() is " + e.message)
        }

    }
}