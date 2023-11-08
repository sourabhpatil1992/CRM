package com.venter.crm.hrMangment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.R
import com.venter.crm.databinding.FragmentHrTimeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HrTimeFragment : Fragment() {

    private var _binding:FragmentHrTimeBinding? = null
    private val binding:FragmentHrTimeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHrTimeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}