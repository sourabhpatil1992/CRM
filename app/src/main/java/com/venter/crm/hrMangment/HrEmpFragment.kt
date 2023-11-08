package com.venter.crm.hrMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHrEmpBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

/**************************************************************
 *
 * HREmployee Fragment In this SHows list of all Employee
 * Can Create an employee Employee and user are different
 * diff is Emp may be not have an access of app
 * Created By : SOurabh Patil
 * Create On : 08 Nov 2023
 *
 ***************************************************************/

@AndroidEntryPoint
class HrEmpFragment : Fragment() {
    private var _binding: FragmentHrEmpBinding? = null
    private val binding: FragmentHrEmpBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentHrEmpBinding.inflate(layoutInflater)

            binding.floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_hrEmpFragment_to_createEmpFragment)
            }

            binding.root

        } catch (e: Exception) {
            Log.d(TAG, "Error in HrEmpFragment.kt is ${e.message}")
            null
        }


    }


}