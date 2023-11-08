package com.venter.crm.hrMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHrDashBinding
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HrDashFragment : Fragment() {
   private var _binding:FragmentHrDashBinding? = null
    private val binding:FragmentHrDashBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentHrDashBinding.inflate(layoutInflater)
            // Inflate the layout for this fragment
            binding.root
        } catch (e:Exception) {
            Log.d(TAG,"Error in HrDashFragment.kt is : ${e.message}")
            null
        }
    }

}