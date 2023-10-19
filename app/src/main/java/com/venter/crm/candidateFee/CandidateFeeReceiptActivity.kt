package com.venter.crm.candidateFee

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
import com.venter.crm.R
import com.venter.crm.databinding.ActivityCandidateFeeReceiptBinding
import com.venter.crm.models.FeeLedger
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class CandidateFeeReceiptActivity : AppCompatActivity() {
    private var _binding: ActivityCandidateFeeReceiptBinding? = null
    private val binding: ActivityCandidateFeeReceiptBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private var candidateFeeLedger: FeeLedger? = null

    private var cId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCandidateFeeReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cId = intent.getStringExtra("id").toString()
        binding.txtName.text = intent.getStringExtra("name").toString()
        candidateFeeLedger = intent.getParcelableExtra<FeeLedger>("rcpt")

        val transArray = arrayOf("Candidate Fee","Transaction Fee")

        val adapter = ArrayAdapter(
            this,
            R.layout.layout_spinneritem, transArray
        )
        binding.spinnerType.adapter = adapter

        if (candidateFeeLedger != null) {
            binding.edtRemark.setText(candidateFeeLedger!!.remark.toString())
            binding.edtAmt.setText(candidateFeeLedger!!.amt.toInt().toString())
            binding.edtRcptDate.setText(candidateFeeLedger!!.date)
            binding.edtNxtPay.setText(intent.getStringExtra("nextDate").toString())
        }

        binding.btnRcptCal.setOnClickListener {
            calView("Receipt")
        }
        binding.btnNxtPayCal.setOnClickListener {
            calView("Next")
        }
        binding.btnSubmit.setOnClickListener {
            submitData()
        }
        val dateFrmt = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        binding.edtRcptDate.setText((LocalDateTime.now().format(dateFrmt)).toString())
        binding.edtNxtPay.setText((LocalDateTime.now().format(dateFrmt)).toString())
    }

    private fun submitData() {
        try {
            if (cId != "" && binding.edtRcptDate.text.isNotEmpty() && binding.edtNxtPay.text.isNotEmpty() && binding.edtRemark.text.isNotEmpty() && binding.edtAmt.text.isNotEmpty()) {
                try {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    dateFormat.parse(binding.edtRcptDate.text.toString())
                    dateFormat.parse(binding.edtNxtPay.text.toString())

                    if (candidateFeeLedger != null) {
                        candidateViewModel.candidateFeeReceipt(
                            cId,
                            binding.edtRcptDate.text.toString(),
                            binding.edtAmt.text.toString(),
                            binding.edtRemark.text.toString(),
                            binding.edtNxtPay.text.toString(),
                            binding.txtName.text.toString(),
                            candidateFeeLedger!!.trans_id,
                            binding.spinnerType.selectedItem.toString()
                        )

                    } else
                        candidateViewModel.candidateFeeReceipt(
                            cId,
                            binding.edtRcptDate.text.toString(),
                            binding.edtAmt.text.toString(),
                            binding.edtRemark.text.toString(),
                            binding.edtNxtPay.text.toString(),
                            binding.txtName.text.toString(),
                            0,
                            binding.spinnerType.selectedItem.toString()
                        )

                    candidateViewModel.stringResData.observe(this)
                    {
                        binding.progressbar.visibility = View.GONE
                        when (it) {
                            is NetworkResult.Loading -> binding.progressbar.visibility =
                                View.VISIBLE

                            is NetworkResult.Error -> Toast.makeText(
                                this,
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                            is NetworkResult.Success -> {
                                Toast.makeText(
                                    this,
                                    "Receipt Saved Successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, AdminDashboard::class.java)
                                startActivity(intent)
                                this.finishAffinity()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Please Insert Date in DD-MM-YYYY format.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateFeeReceiptActivity.kt submitData() is " + e.message)
        }
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
                if (date == "Receipt")
                    binding.edtRcptDate.setText(formatted)
                else
                    binding.edtNxtPay.setText(formatted)

            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in CandidateFeeReceiptActivity.kt calView() is " + e.message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}