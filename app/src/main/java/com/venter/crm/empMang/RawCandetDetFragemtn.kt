package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.crm.databinding.FragmentRawCandetDetFragemtnBinding
import com.venter.crm.models.RawCandidateData
import com.venter.crm.utils.Constans.TAG
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint

class RawCandetDetFragemtn : Fragment() {
    private var _binding: FragmentRawCandetDetFragemtnBinding? = null
    private val binding: FragmentRawCandetDetFragemtnBinding
        get() = _binding!!
    private lateinit var act: RawDataDetActivity


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as RawDataDetActivity

        setView(act.data)
    }

    private fun setView(data: RawCandidateData?) {
        try {

            binding.txtCandidateName.text = data?.candidate_name
            binding.txtMobNo.text = data?.mob_no
            binding.txtAlterMobNo.text = data?.altenate_mobno
            binding.txtEmail.text = data?.emailId
            binding.txtLocation.text = data?.curr_location
            binding.txtScrApp.text = data?.SourceOfApplication
            binding.txtDateapp.text = data?.appDate
            //binding.txtQualification.text = data?.Qualification
            //binding.txtPassing.text = data?.PassYear
            if(data?.trader == 1) {
                binding.txtTrader.text = "Yes"
                binding.txtCapital.text = data.capital.toString()
                binding.txtSegment.text = data.segment
                binding.linTrader.visibility = View.VISIBLE

            }else {
                binding.txtTrader.text = "No"
                binding.linTrader.visibility = View.GONE
            }


            val lastComment = data?.commentList?.firstOrNull()
            val followupDate = lastComment?.follloupDate

            binding.txtFollowup.text = if (followupDate != null && followupDate != "01-01-1900") {
                followupDate
            } else {
                ""
            }
                //data?.commentList?.get(data?.commentList.lastIndex)?.follloupDate ?: ""


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