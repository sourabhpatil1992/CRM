package com.venter.regodigital.EmployeeMangment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.databinding.ActivityTelecallerReportBinding
import com.venter.regodigital.models.EmpReport
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TelecallerReportActivity : AppCompatActivity() {
    private var _binding:ActivityTelecallerReportBinding? = null
    private val binding:ActivityTelecallerReportBinding
    get() = _binding!!


    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTelecallerReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        binding.txtFromDate.text = df.format(c)
        binding.txtToDate.text = df.format(c)

        candidateViewModel.getTelReport(df.format(c),df.format(c))
        candidateViewModel.empReptResLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading ->  binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                is NetworkResult.Success -> setView(it.data!!)
            }
        }

        binding.txtToDate.setOnClickListener {
            setDate(binding.txtToDate)
        }
        binding.txtFromDate.setOnClickListener {
            setDate(binding.txtFromDate)
        }



    }

    private fun setDate(txtFromDate: TextView) {
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("SELECT A DATE")


        val materialDatePicker = materialDateBuilder.build()


        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            val formatted = format.format(utc.time).toString()
            txtFromDate.setText(formatted)

            candidateViewModel.getTelReport(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())
        }


    }

    private fun setView(data: EmpReport) {
        try{
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val today: String = df.format(c)

            //Count the Time
            val sec = data.callTime%60
            var min = (data.callTime/60).toInt()
            var times = ""
            if(min>60) {
                val hrs = (min/60).toInt()
                min %= 60

                times = "$hrs hrs $min min $sec sec"
            }
            else
                times = "$min min $sec sec"


            //Set The Manual Data
            binding.txtTotalCall.text = data.totalCall.toString()
            binding.txtTotalAdmi.text = data.admissions.toString()
            binding.txtTotalProspect.text = data.prospects.toString()
            binding.txtTotalTime.text = times


            //Set Today's Data
            if(binding.txtFromDate.text == today && binding.txtToDate.text == today)
            {
                binding.txtTodaysCall.text = data.totalCall.toString()
                binding.txtTodaysAds.text = data.admissions.toString()
                binding.txtTodayPros.text = data.prospects.toString()
                binding.txtTodaysTime.text = times
            }





        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in EmpReportFragment.kt setView() is "+e.message)
        }

    }
}