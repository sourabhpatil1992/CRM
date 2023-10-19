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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.venter.crm.R
import com.venter.crm.databinding.FragmentEmployeeDashBinding
import com.venter.crm.utils.Constans
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeDashFragment : Fragment() {

    private var _binding:FragmentEmployeeDashBinding? = null
    private val binding:FragmentEmployeeDashBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeDashBinding.inflate(layoutInflater)

        try{

            if(!permissionGranted(requireContext()))
            {
                askPermission()
            }

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
                Log.d(TAG,"----------------------------")
                findNavController().navigate(R.id.action_employeeDashFragment_to_stalDataFragment)
            }


        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmployeeDashFragment.kt is ${e.message}")
        }


        return binding.root
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
                requireActivity(),
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