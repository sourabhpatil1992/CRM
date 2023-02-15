package com.venter.regodigital.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.regodigital.Candidate.CandidateMang
import com.venter.regodigital.candidateFee.FeeDashboard
import com.venter.regodigital.databinding.ActivityAdminDashboardBinding
import com.venter.regodigital.officeexpenses.ExpensesDashboardActivity
import com.venter.regodigital.userledger.UserLedgerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboard : AppCompatActivity() {
    private var _binding:ActivityAdminDashboardBinding? = null
    private val binding:ActivityAdminDashboardBinding
    get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.candidateManagement.setOnClickListener {
            val intent = Intent(this,CandidateMang::class.java)
            startActivity(intent)
        }

        binding.candidateAccount.setOnClickListener {
            val intent = Intent(this,FeeDashboard::class.java)
            startActivity(intent)
        }

        binding.officeExpenses.setOnClickListener{
            val intent = Intent(this,ExpensesDashboardActivity::class.java)
            startActivity(intent)
        }

        binding.userLedger.setOnClickListener {
            val intent = Intent(this,UserLedgerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}