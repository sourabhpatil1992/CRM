package com.venter.crm.Dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.venter.crm.EmployeeMangment.AdminViewPagerAdapter
import com.venter.crm.EmployeeMangment.ViewPagerAdapter
import com.venter.crm.R
import com.venter.crm.databinding.FragmentEmployeeDashBinding
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.BASE_URL
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class EmployeeDashFragment : Fragment() {

    private var _binding: FragmentEmployeeDashBinding? = null
    private val binding: FragmentEmployeeDashBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    @Inject
    lateinit var tokenManger: TokenManger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDashBinding.inflate(layoutInflater)

        try {

            if (!permissionGranted(requireContext())) {
                askPermission()
            }

            //officeClose()

            getTime()



            Picasso.get()
                .load("${BASE_URL}assets/dashResource/${tokenManger.getInstituteId()}.jpg")
                .into(binding.dashImage)



        } catch (e: Exception) {
            Log.d(TAG, "Error in EmployeeDashFragment.kt is ${e.message}")
        }


        return binding.root
    }

    private fun getTime() {
        try {
            candidateViewModel.getTodaysShedule()
            candidateViewModel.todaySheduleResLiveData.observe(viewLifecycleOwner)
            {

                when (it) {
                    is NetworkResult.Loading -> {}
                    is NetworkResult.Error -> Toast.makeText(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        //Log.d(TAG,it.data.toString())
                        getData(it.data!!.startTime, it.data!!.endTime, it.data!!.uStatus)
                    }
                }

            }

        } catch (e: Error) {
            Log.d(TAG, "Error in EmpDashboard.kt onCreate() is " + e.message)
        }
    }

    private fun getData(startTime: String, endTime: String, uStatus: Int) {
        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern)
        val calendar1 = Calendar.getInstance()
        val currentTime = sdf.parse(sdf.format(calendar1.getTime()))

        val date1: Date = sdf.parse(startTime)
        val date2: Date = sdf.parse(endTime)
        if (tokenManger.getUserType() == "Admin") {
            if (uStatus == 1)
                startOffice()
            else
                officeClose()

        } else {
            if (!currentTime.before(date1) && currentTime.before(date2) && uStatus == 1) {

                startOffice()

            } else {
                officeClose()
            }
        }

    }

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
                        ) != PackageManager.PERMISSION_GRANTED)
                )

    }

    private fun askPermission() {
        try {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS
                ), 500
            )

        } catch (e: Exception) {
            Log.d(Constans.TAG, "Error in the asking permission..." + e.message)
        }


    }

    private fun startOffice() {
        binding.rawdata.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_rawDataFragment)
        }
        binding.incomming.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_incomingLeads)
        }

        binding.notres.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_empNotRespondingData)
        }

        binding.colddata.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_coldDataFragment)
        }
        binding.folloup.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_empFolloupCandFragment)
        }
        binding.stealData.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_stalDataFragment)
        }
        binding.prospectdata.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_empProsFragement)
        }
        binding.callLog.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_callLogFragment)
        }
        binding.setting.setOnClickListener {
            findNavController().navigate(R.id.action_employeeDashFragment_to_empSettingFragment)
        }

    }

    private fun officeClose() {
        binding.rawdata.setOnClickListener {
            msgBox()
        }
        binding.incomming.setOnClickListener {
            msgBox()
        }
        binding.notres.setOnClickListener {
            msgBox()
        }

        binding.colddata.setOnClickListener {
            msgBox()
        }
        binding.folloup.setOnClickListener {
            msgBox()
        }
        binding.stealData.setOnClickListener {
            msgBox()
        }
        binding.prospectdata.setOnClickListener {
            msgBox()
        }
        binding.callLog.setOnClickListener {
            msgBox()
        }
        binding.setting.setOnClickListener {
            msgBox()
        }
    }

    private fun msgBox() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Office Closed")
        builder.setIcon(resources.getDrawable(R.drawable.crm))
        builder.setMessage("Office time is closed. Please try during office hours.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

}