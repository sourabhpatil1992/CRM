package com.venter.crm.empMang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.R
import com.venter.crm.databinding.FragmentEmpSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmpSettingFragment : Fragment() {

    private var _binding:FragmentEmpSettingBinding? = null
    private val binding:FragmentEmpSettingBinding
        get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentEmpSettingBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }


}