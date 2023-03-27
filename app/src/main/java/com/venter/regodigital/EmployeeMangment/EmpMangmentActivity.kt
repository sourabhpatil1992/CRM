package com.venter.regodigital.EmployeeMangment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.databinding.ActivityEmpMangmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmpMangmentActivity : AppCompatActivity() {
    private var _binding:ActivityEmpMangmentBinding? = null
    private val binding:ActivityEmpMangmentBinding
    get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmpMangmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rawData.setOnClickListener {
            val intent = Intent(this,RawDataActivity::class.java)
            startActivity(intent)
        }

        binding.empDashboard.setOnClickListener {
            val intent = Intent(this,EmpDashboard::class.java)
            startActivity(intent)
        }

        binding.teleReport.setOnClickListener {
            val intent = Intent(this,TelecallerReportActivity::class.java)
            startActivity(intent)
        }
        binding.admission.setOnClickListener {
            val intent = Intent(this,AsmissionDataActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}