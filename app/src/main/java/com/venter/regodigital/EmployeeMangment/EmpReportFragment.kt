package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.databinding.FragmentEmpReportBinding
import com.venter.regodigital.models.EmpReport
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EmpReportFragment : Fragment() {

    private var _binding:FragmentEmpReportBinding? =  null
    private val binding:FragmentEmpReportBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentEmpReportBinding.inflate(layoutInflater)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        binding.txtFromDate.text = df.format(c)
        binding.txtToDate.text = df.format(c)


        candidateViewModel.getEmpReport(df.format(c),df.format(c))
        candidateViewModel.empReptResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading ->  binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                is NetworkResult.Success -> setView(it.data!!)
            }
        }

        binding.txtToDate.setOnClickListener {
            setDate(binding.txtToDate)
        }
        binding.txtFromDate.setOnClickListener {
            setDate(binding.txtFromDate)
        }



        return binding.root
    }

    private fun setDate(txtFromDate: TextView) {
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("SELECT A DATE")


        val materialDatePicker = materialDateBuilder.build()


        materialDatePicker.show(requireActivity().supportFragmentManager, "MATERIAL_DATE_PICKER")

        materialDatePicker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

            val formatted = format.format(utc.time).toString()
            txtFromDate.setText(formatted)

            candidateViewModel.getEmpReport(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())
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
            Log.d(TAG,"Error in EmpReportFragment.kt setView() is "+e.message)
        }

    }


}