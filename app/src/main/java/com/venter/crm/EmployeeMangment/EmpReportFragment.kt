package com.venter.crm.EmployeeMangment

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.databinding.FragmentEmpReportBinding
import com.venter.crm.models.EmpReport
import com.venter.crm.models.UserList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class EmpReportFragment : Fragment() {

    private var _binding:FragmentEmpReportBinding? =  null
    private val binding:FragmentEmpReportBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    var userDataList: ArrayList<UserList> = ArrayList()

    @Inject
    lateinit var tokenManger: TokenManger


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentEmpReportBinding.inflate(layoutInflater)

        userDataList = ArrayList()

        val empList: ArrayList<String> = ArrayList()


        empList.add("You")

        val adapters = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.select_dialog_item,
            empList
        )
        binding.spinEmpName.adapter = adapters

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        binding.txtFromDate.text = df.format(c)
        binding.txtToDate.text = df.format(c)
        if (tokenManger.getUserType().toString() != "Employee") {
            getData()
        }
        else
        {
            getEmpData(tokenManger.getUserId().toString())
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
            txtFromDate.text = formatted

            if(binding.spinEmpName.selectedItem != null && binding.spinEmpName.selectedItem.toString() != "You") {
                var othersUserId = "0"
                userDataList.forEach {
                    if (it.user_name.contains(
                            binding.spinEmpName.selectedItem.toString(),
                            true
                        )
                    )
                        othersUserId = it.id.toString()
                }
                getEmpData(othersUserId)
            }
            else
                getEmpData(tokenManger.getUserId().toString())
           // candidateViewModel.getEmpReport(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString(),)
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
            binding.txtTotalProspect.text = "${data.prospects} + ${data.tprospects}"
            binding.txtTotalTime.text = times


            //Set Today's Data
            if(binding.txtFromDate.text == today && binding.txtToDate.text == today)
            {
                binding.txtTodaysCall.text = data.totalCall.toString()
                binding.txtTodaysAds.text = data.admissions.toString()
                binding.txtTodayPros.text = "${data.prospects} + ${data.tprospects}"//data.prospects.toString()
                binding.txtTodaysTime.text = times
            }





        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmpReportFragment.kt setView() is "+e.message)
        }

    }

    private fun getData() {
        try {
            binding.linEmpSpin.visibility = View.VISIBLE
            candidateViewModel.getEmpListRawData()
            candidateViewModel.userListLiveData.observe(viewLifecycleOwner)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            if (it.data != null) {
                                userDataList = (it.data as ArrayList<UserList>?)!!
                                val empList: ArrayList<String> = ArrayList()

                                empList.add("You")

                                it.data.forEach { data ->
                                    if (data.id.toString() != tokenManger.getUserId().toString())
                                        empList.add(data.user_name)
                                }
                                val adapters = ArrayAdapter<String>(
                                    requireContext(),
                                    R.layout.select_dialog_item,
                                    empList
                                )
                                binding.spinEmpName.adapter = adapters

                                if(empList.isNotEmpty())
                                binding.spinEmpName.onItemSelectedListener = object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        if(binding.spinEmpName.selectedItem.toString() != "You") {
                                            var othersUserId = "0"
                                            userDataList.forEach {
                                                if (it.user_name.contains(
                                                        binding.spinEmpName.selectedItem.toString(),
                                                        true
                                                    )
                                                )
                                                    othersUserId = it.id.toString()
                                            }
                                            getEmpData(othersUserId)
                                        } else
                                            getEmpData(tokenManger.getUserId().toString())
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {
                                        return
                                    }
                                }

                            }
                        }
                        catch (e:Exception)
                        {
                            Log.d(TAG,"Error in EmpReportsFrgmant.kt getData() is  "+e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            context,
                            "Something went wrong.Please contact the System Administrator.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                }
            }
        }catch (e: Exception) {
            Log.d(TAG, "Error in EmpReportFragment.kt getData() is " + e.message)
        }

    }

    private fun getEmpData(empId: String)
    {
        candidateViewModel.getEmpReport(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString(),empId)
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
    }


}