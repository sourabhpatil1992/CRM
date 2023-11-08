package com.venter.crm.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.venter.crm.R
import com.venter.crm.databinding.ActivityAdminProfileBinding
import com.venter.crm.databinding.ActivityAdminReportDashBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminProfileActivity : AppCompatActivity() {

    private var _binding:ActivityAdminProfileBinding? = null
    private val binding:ActivityAdminProfileBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in AdminProfileActivity.kt is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       // _binding = null
    }
}