package com.venter.regodigital.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.venter.regodigital.databinding.ActivityEmpDashboardBinding

class EmpDashboard : AppCompatActivity() {
    private var _binding:ActivityEmpDashboardBinding? = null
    private val binding:ActivityEmpDashboardBinding
    get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmpDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}