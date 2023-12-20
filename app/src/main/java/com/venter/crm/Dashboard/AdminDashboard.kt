package com.venter.crm.Dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.venter.crm.EmployeeMangment.EmpMangmentActivity
import com.venter.crm.databinding.ActivityAdminDashboardBinding
import com.venter.crm.hrMangment.HrActivity
import com.venter.crm.integration.ConfigurationActivity
import com.venter.crm.integration.IntegrationActivity
import com.venter.crm.reportMangment.AdminReportDash
import com.venter.crm.utils.Constans
import com.venter.crm.utils.TokenManger
import com.venter.crm.whatsTemp.WhatsappTemplates
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminDashboard : AppCompatActivity() {
    private var _binding: ActivityAdminDashboardBinding? = null
    private val binding: ActivityAdminDashboardBinding
        get() = _binding!!

    @Inject
    lateinit var tokenManger: TokenManger

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Picasso.get()
            .load("${Constans.BASE_URL}assets/dashResource/${tokenManger.getInstituteId()}.jpg")
            .into(binding.dashImage)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!permissionGranted(this)) {
                askPermission()
            }
        }
        else{
            if (!permissionGranted12(this)) {
                askPermission12()
            }
        }



        binding.report.setOnClickListener {
            val intent = Intent(this, AdminReportDash::class.java)
            startActivity(intent)
            //Toast.makeText(this,"Coming Soon....",Toast.LENGTH_LONG).show()
        }

        binding.empMang.setOnClickListener {
            val intent = Intent(this, EmpMangmentActivity::class.java)
            startActivity(intent)
        }


        binding.msgTemp.setOnClickListener {
            val intent = Intent(this, WhatsappTemplates::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.hr.setOnClickListener {
            val intent = Intent(this, HrActivity::class.java)
            startActivity(intent)
        }
        binding.integrations.setOnClickListener {
            val intent = Intent(this, IntegrationActivity::class.java)
            startActivity(intent)
        }
        binding.configration.setOnClickListener {
            val intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun permissionGranted(context: Context): Boolean {

        return !(
                (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_PHONE_NUMBERS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.SEND_SMS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.FOREGROUND_SERVICE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.PROCESS_OUTGOING_CALLS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_VIDEO
                        ) != PackageManager.PERMISSION_GRANTED)
                )

    }
    private fun permissionGranted12(context: Context): Boolean {

        return !(
                (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_PHONE_NUMBERS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.SEND_SMS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.FOREGROUND_SERVICE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.PROCESS_OUTGOING_CALLS
                        ) != PackageManager.PERMISSION_GRANTED) or
                        (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED)
                )

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askPermission() {
        try {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,

                    ), 500
            )

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in the asking permission..." + e.message)
        }


    }

    private fun askPermission12() {
        try {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.CALL_PHONE

                    ), 500
            )

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in the asking permission..." + e.message)
        }


    }


}