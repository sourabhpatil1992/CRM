package com.venter.crm.EmployeeMangment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.crm.Dashboard.EmpDashboard
import com.venter.crm.Dashboard.EmployeeDash
import com.venter.crm.databinding.ActivityEmpMangmentBinding
import com.venter.crm.empMang.AdminRawDataActivity
import com.venter.crm.userledger.UserLedgerActivity
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
            //val intent = Intent(this,RawDataActivity::class.java)
            val intent = Intent(this,AdminRawDataActivity::class.java)
            startActivity(intent)
        }

        binding.empDashboard.setOnClickListener {
            //val intent = Intent(this,EmpDashboard::class.java)
            val intent = Intent(this,EmployeeDash::class.java)
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
        binding.officeHrs.setOnClickListener {
            val intent = Intent(this,WorkingHourseAcivity::class.java)
            startActivity(intent)
        }
        binding.userLedger.setOnClickListener {
            val intent = Intent(this, UserLedgerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}