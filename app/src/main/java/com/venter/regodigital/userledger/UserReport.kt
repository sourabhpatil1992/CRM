package com.venter.regodigital.userledger

import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.venter.regodigital.R
import com.venter.regodigital.databinding.ActivityUserReportBinding
import com.venter.regodigital.models.UserReportData
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class UserReport : AppCompatActivity() {
    private  var _binding:ActivityUserReportBinding? = null
    private val binding:ActivityUserReportBinding
        get() = _binding!!
    private var userId = 0

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private var reportList: ArrayList<UserReportData> = ArrayList()

    private lateinit var adapter: UserReportAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = UserReportAdapter()
        userId = intent.getIntExtra("userId",0)



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

       getData()
    }

    private fun getData() {
        candidateViewModel.getUserReport(
            userId,
            binding.fromDate.text.toString(),
            binding.toDate.text.toString()
        )

        candidateViewModel.userReportListResLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE
            when(it)
            {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
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
            Log.d(TAG,"Error in UserReport.kt setDat() is ${e.message}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
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


        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

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
            Log.d(TAG,"Error in UserReport.kt setDate() is ${e.message}")
        }
    }
}