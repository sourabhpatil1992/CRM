package com.venter.crm.officeexpenses

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.Dashboard.AdminDashboard
import com.venter.crm.databinding.ActivityExpenseRecptBinding
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ExpenseRecptActivity : AppCompatActivity()
{
    private var _binding:ActivityExpenseRecptBinding? =null
    private val binding:ActivityExpenseRecptBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private val arrayTrantype = arrayOf("Utility Bills","Office Rent","Employee Salary","Credit in Office Account","Other")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityExpenseRecptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, arrayTrantype)

        binding.spinnerTransType.adapter = adapter
        val dateFrmt = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        binding.edtRcptDate.setText((LocalDateTime.now().format(dateFrmt)).toString())

        binding.btnRcptCal.setOnClickListener {
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

                    binding.edtRcptDate.setText(formatted)


                }
            }
            catch (e:Exception)
            {
                Log.d(TAG,"Error in ExpensesRecptActivity.kt binding.btnRcptCal.setOnClickListener() is "+e.message)
            }
        }

        binding.btnSubmit.setOnClickListener { submitAction() }

    }

    private fun submitAction() {
        if(binding.edtRcptDate.text.length == 10 && binding.edtAmt.text.isNotEmpty() )
        {
            try {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                dateFormat.parse(binding.edtRcptDate.text.toString())

                val category = if(binding.rdobtnDebit.isChecked)
                    "Debit"
                else
                    "Credit"
                candidateViewModel.expensesReceipt(binding.edtRcptDate.text.toString(),category,binding.spinnerTransType.selectedItem.toString(),binding.edtRcptDscr.text.toString(),binding.edtAmt.text.toString())
                candidateViewModel.stringResData.observe(this)
                {
                    binding.progressbar.visibility = View.GONE
                    when(it)
                    {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                        is NetworkResult.Success -> {
                            Toast.makeText(this,"Transaction Registered Successfully.",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,AdminDashboard::class.java)
                            startActivity(intent)
                        }
                    }
                }

            }
            catch (e:Exception){
                Log.d(TAG,"Error in ExpenseRecptActivity.kt submitaction() is "+e.message)
                Toast.makeText(this,"Please fill all details correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}