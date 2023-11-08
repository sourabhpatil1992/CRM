package com.venter.crm.reportMangment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.venter.crm.R
import com.venter.crm.databinding.ActivityAdminReportDashBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

/********************************************************
 * Tis is dashboard of the Report VIew Shows the multiple Report view here
 * Created_By :Sourabh
 * On 28th Oct 2023
 ********************************************************/
@AndroidEntryPoint
class AdminReportDash : AppCompatActivity() {
    private var _binding: ActivityAdminReportDashBinding? = null
    private val binding: ActivityAdminReportDashBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminReportDashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            binding.leadReport.setOnClickListener {
                Toast.makeText(
                    this,
                    "This feature is not available in your plan. For use this service please upgrade your plan.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.marketReport.setOnClickListener {
                Toast.makeText(
                    this,
                    "This feature is not available in your plan. For use this service please upgrade your plan.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.empReport.setOnClickListener {
                Toast.makeText(
                    this,
                    "This feature is not available in your plan. For use this service please upgrade your plan.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.finReport.setOnClickListener {
                Toast.makeText(
                    this,
                    "This feature is not available in your plan. For use this service please upgrade your plan.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in AdminReportDash.kt is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}