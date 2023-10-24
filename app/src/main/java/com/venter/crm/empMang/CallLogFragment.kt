package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.crm.R
import com.venter.crm.databinding.ActivityUserReportBinding
import com.venter.crm.databinding.FragmentCallLogBinding
import com.venter.crm.models.UserList
import com.venter.crm.models.UserReportData
import com.venter.crm.userledger.UserReportAdapter
import com.venter.crm.utils.Constans
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class CallLogFragment : Fragment() {
    private  var _binding: FragmentCallLogBinding? = null
    private val binding: FragmentCallLogBinding
        get() = _binding!!
    private var userId :Int?= 0

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var reportList: ArrayList<UserReportData> = ArrayList()

    var userDataList: ArrayList<UserList> = ArrayList()

    private lateinit var adapter: UserReportAdapter

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCallLogBinding.inflate(layoutInflater)

        adapter = UserReportAdapter()
        userId = tokenManger.getUserId()?.toIntOrNull()



        val today = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now())

        binding.toDate.text = today
        binding.fromDate.text = today

        binding.btnFilter.setOnClickListener {
            showPopupMenu(it)
        }

        binding.fromDate.setOnClickListener {
            setDate(binding.fromDate)
        }
        binding.toDate.setOnClickListener {
            setDate(binding.toDate)
        }

        spinnerChange()



        setEmployeeSpinner()

        getData()
        return binding.root
    }

    private fun getData() {
        if (binding.spinEmpName.selectedItem == "You") {
            candidateViewModel.getUserReport(
                tokenManger.getUserId()!!.toInt(),
                binding.fromDate.text.toString(),
                binding.toDate.text.toString()
            )

        } else {
            var othersUserId = 0
            userDataList.forEach {
                if (it.user_name.equals(
                        binding.spinEmpName.selectedItem.toString(),
                        ignoreCase = true
                    )
                )
                    othersUserId = it.id
            }
            candidateViewModel.getUserReport(
                othersUserId,
                binding.fromDate.text.toString(),
                binding.toDate.text.toString()
            )

        }


        candidateViewModel.userReportListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                is NetworkResult.Success -> {
                    reportList = it.data as ArrayList<UserReportData>
                    setData(reportList)
                    val call =  reportList.map{it.callTime}.sum()
                    val hours = call / 3600
                    val minutes = (call % 3600) / 60
                    val remainingSeconds = call % 60

                    binding.callTime.text =  String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
                    binding.calls.text = reportList.size.toString()
                }
            }
        }
    }

    private fun setData(data: List<UserReportData>) {
        try {

            adapter.submitList(null)
            adapter.notifyDataSetChanged()
            adapter.submitList(data)
            binding.rcView.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcView.adapter = adapter
        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in UserReport.kt setDat() is ${e.message}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            // Handle the selected item here
            when (item.itemId) {
                R.id.option1 -> {

                    var data
                            = reportList.sortedBy { it.callTime }
                    setData(data)

                    true
                }
                R.id.option2 -> {
                    var data= reportList.sortedBy { it.candidate_name }
                    setData(data)
                    // Do something when Option 2 is selected
                    true
                }
                R.id.option3 -> {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    var data =  reportList.sortedBy { dateFormat.parse(it.created_on) }
                    setData(data)
                    // Do something when Option 2 is selected
                    true
                }
                R.id.option4 -> {

                    var data =  reportList.sortedBy {  it.prosType }
                    setData(data)
                    // Do something when Option 2 is selected
                    true
                }
                // Add more cases for other options as needed
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun setDate(date: TextView) {
        try{
            val materialDateBuilder: MaterialDatePicker.Builder<*> =
                MaterialDatePicker.Builder.datePicker()

            materialDateBuilder.setTitleText("SELECT A DATE")


            val materialDatePicker = materialDateBuilder.build()


            materialDatePicker.show(requireFragmentManager(), "MATERIAL_DATE_PICKER")

            materialDatePicker.addOnPositiveButtonClickListener {

                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

                val formatted = format.format(utc.time).toString()
                date.setText(formatted)
                getData()

            }


        }
        catch (e:Exception)
        {
            Log.d(Constans.TAG,"Error in UserReport.kt setDate() is ${e.message}")
        }
    }

    private fun spinnerChange() {

        binding.spinEmpName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {

                    getData()
                } catch (e: Exception) {
                    Log.d(
                        Constans.TAG,
                        "Error in IncomingLeads setEmployeeSpinner() onItemSelectedListener is : ${e.message}"
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


    }

    private fun setEmployeeSpinner() {
        candidateViewModel.getEmpListRawData()

        candidateViewModel.userListLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Success -> {
                    binding.progressbar.visibility = View.GONE
                    val empList: ArrayList<String> = ArrayList()
                    empList.add("You")
                    userDataList.clear()

                    result.data?.forEach { data ->
                        userDataList.add(data)
                        if (data.id.toString() != tokenManger.getUserId().toString()) {
                            empList.add(data.user_name)
                        }
                    }

                    val adapters = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.select_dialog_item,
                        empList
                    )
                    binding.spinEmpName.adapter = adapters

                }
            }
        }
    }


}