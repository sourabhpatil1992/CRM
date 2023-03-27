package com.venter.regodigital.Dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.venter.regodigital.Candidate.CandidateMang
import com.venter.regodigital.EmployeeMangment.EmpMangmentActivity
import com.venter.regodigital.candidateFee.FeeDashboard
import com.venter.regodigital.databinding.ActivityAdminDashboardBinding
import com.venter.regodigital.officeexpenses.ExpensesDashboardActivity
import com.venter.regodigital.userledger.UserLedgerActivity
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.whatsTemp.WhatsappTemplates
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

        if(!permissionGranted(this))
        {
            askPermission()
        }


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

        binding.empMang.setOnClickListener {
            val intent = Intent(this,EmpMangmentActivity::class.java)
            startActivity(intent)
        }

        binding.msgTemp.setOnClickListener {
            val intent = Intent(this,WhatsappTemplates::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun permissionGranted(context: Context): Boolean
    {

        return !(
                (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS)!= PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.FOREGROUND_SERVICE)!= PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED)
                )

    }

    private fun askPermission()
    {
        try
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS
                ), 500)

        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in the asking permission..."+e.message)
        }


    }
}