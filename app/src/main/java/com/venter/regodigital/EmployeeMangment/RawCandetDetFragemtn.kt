package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentRawCandetDetFragemtnBinding
import com.venter.regodigital.models.RawCandidateData
import com.venter.regodigital.utils.Constans.TAG


class RawCandetDetFragemtn : Fragment() {
    private var _binding: FragmentRawCandetDetFragemtnBinding? = null
    private val binding: FragmentRawCandetDetFragemtnBinding
        get() = _binding!!
    private lateinit var act: RawDataDetActivity


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as RawDataDetActivity

        setView(act.data!!)
    }

    private fun setView(data: RawCandidateData) {
        try {
            binding.txtCandidateName.text = data.candidate_name
            binding.txtMobNo.text = data.mob_no
            binding.txtEmail.text = data.emailId
            binding.txtLocation.text = data.curr_location
            binding.txtScrApp.text = data.SourceOfApplication
            binding.txtDateapp.text = data.appDate
            binding.txtQualification.text = data.Qualification
            binding.txtPassing.text = data.PassYear


        } catch (e: Exception) {
            Log.d(TAG, "Error in RawCandetDetFragment.kt setView() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRawCandetDetFragemtnBinding.inflate(layoutInflater)


        return binding.root
    }


}