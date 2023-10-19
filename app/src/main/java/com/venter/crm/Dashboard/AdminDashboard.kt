package com.venter.crm.Dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.venter.crm.Candidate.CandidateMang
import com.venter.crm.EmployeeMangment.EmpMangmentActivity
import com.venter.crm.candidateFee.FeeDashboard
import com.venter.crm.databinding.ActivityAdminDashboardBinding
import com.venter.crm.officeexpenses.ExpensesDashboardActivity
import com.venter.crm.userledger.UserLedgerActivity
import com.venter.crm.utils.Constans
import com.venter.crm.whatsTemp.WhatsappTemplates
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



        binding.report.setOnClickListener {
            //val intent = Intent(this,UserLedgerActivity::class.java)
           // startActivity(intent)
            Toast.makeText(this,"Coming Soon....",Toast.LENGTH_LONG).show()
        }

        binding.empMang.setOnClickListener {
            val intent = Intent(this,EmpMangmentActivity::class.java)
            startActivity(intent)
        }

        binding.msgTemp.setOnClickListener {
            val intent = Intent(this,WhatsappTemplates::class.java)
            startActivity(intent)
        }

        binding.hr.setOnClickListener {
            Toast.makeText(this,"Coming Soon....",Toast.LENGTH_LONG).show()
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
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) or
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