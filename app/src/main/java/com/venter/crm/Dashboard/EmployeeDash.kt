package com.venter.crm.Dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.venter.crm.R
import com.venter.crm.databinding.ActivityEmployeeDashBinding
import com.venter.crm.models.ProsSubType
import com.venter.crm.utils.Constans
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeDash : AppCompatActivity() {
    private var _binding:ActivityEmployeeDashBinding?=null
    private val binding:ActivityEmployeeDashBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    var prosSubType:List<ProsSubType>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmployeeDashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!permissionGranted(this))
        {
            askPermission()
        }

        candidateViewModel.getProsSubConfig()
        candidateViewModel.prosSubTypeDataLiveData.observe(this)
        {
           // binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> {}//binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(
                    this,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                is NetworkResult.Success -> {
                    prosSubType = it.data
                }
            }
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
                        (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)or
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