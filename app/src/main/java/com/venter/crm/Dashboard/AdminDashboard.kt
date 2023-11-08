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
import com.squareup.picasso.Picasso
import com.venter.crm.Candidate.CandidateMang
import com.venter.crm.EmployeeMangment.EmpMangmentActivity
import com.venter.crm.candidateFee.FeeDashboard
import com.venter.crm.databinding.ActivityAdminDashboardBinding
import com.venter.crm.hrMangment.HrActivity
import com.venter.crm.officeexpenses.ExpensesDashboardActivity
import com.venter.crm.reportMangment.AdminReportDash
import com.venter.crm.userledger.UserLedgerActivity
import com.venter.crm.utils.Constans
import com.venter.crm.utils.TokenManger
import com.venter.crm.whatsTemp.WhatsAppAccounts
import com.venter.crm.whatsTemp.WhatsappTemplates
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminDashboard : AppCompatActivity() {
    private var _binding:ActivityAdminDashboardBinding? = null
    private val binding:ActivityAdminDashboardBinding
    get() = _binding!!

    @Inject
    lateinit var tokenManger: TokenManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Picasso.get()
            .load("${Constans.BASE_URL}assets/dashResource/${tokenManger.getInstituteId()}.jpg")
            .into(binding.dashImage)

        if(!permissionGranted(this))
        {
            askPermission()
        }



        binding.report.setOnClickListener {
            val intent = Intent(this,AdminReportDash::class.java)
            startActivity(intent)
            //Toast.makeText(this,"Coming Soon....",Toast.LENGTH_LONG).show()
        }

        binding.empMang.setOnClickListener {
            val intent = Intent(this,EmpMangmentActivity::class.java)
            startActivity(intent)
        }

        binding.whatsApp.setOnClickListener {
            val intent = Intent(this,WhatsAppAccounts::class.java)
            startActivity(intent)
        }
        binding.msgTemp.setOnClickListener {
            val intent = Intent(this,WhatsappTemplates::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.hr.setOnClickListener {
            val intent = Intent(this,HrActivity::class.java)
            startActivity(intent)
        }
        binding.marketing.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is not available in your plan. For use this service please upgrade your plan.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.kyc.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is not available in your plan. For use this service please upgrade your plan.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.bot.setOnClickListener {
            Toast.makeText(
                this,
                "This feature is not available in your plan. For use this service please upgrade your plan.",
                Toast.LENGTH_SHORT
            ).show()
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