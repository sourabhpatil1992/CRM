package com.venter.regodigital.Dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.venter.regodigital.EmployeeMangment.ViewPagerAdapter
import com.venter.regodigital.databinding.ActivityEmpDashboardBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EmpDashboard : AppCompatActivity() {
    private var _binding:ActivityEmpDashboardBinding? = null
    private val binding:ActivityEmpDashboardBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    var rawData: List<RawDataList>? = null

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmpDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(!permissionGranted(this))
        {
            askPermission()
        }
        //getData()
        if (tokenManger.getUserType() == "Admin") {
        getData()
        }
        else
        {


            try {
                val pattern = "HH:mm"
                val sdf = SimpleDateFormat(pattern)
                val calendar1 = Calendar.getInstance()
                val currentTime = sdf.parse(sdf.format(calendar1.getTime()))
                val time = "09:50"
                val endtime = "19:30"
                val date1: Date = sdf.parse(time)
                val date2: Date = sdf.parse(endtime)

                //Log.d(TAG,"   "+currentTime+"  "+time+"  "+endtime+"   "+currentTime.before(date1)+"   "+currentTime.before(date2))

                if (!currentTime.before(date1) && currentTime.before(date2))
                {
                    getData()
                }
                else
                {
                    Toast.makeText(this,"Please login in Office Time",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Error) {
                Log.d(TAG,"Error in EmpDashboard.kt onCreate() is "+e.message)
            }

        }


    }

     fun getData() {
        try{

            candidateViewModel.getEmpRawData()
            candidateViewModel.allrawDataListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it){
                    is NetworkResult.Loading ->{
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success ->{
                        rawData =it.data
                        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
                        binding.tabLayout.setupWithViewPager(binding.viewPager)

                    }
                }
                

            }
        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in EmpDashboard.kt getData() is "+e.message)
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