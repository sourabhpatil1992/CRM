package com.venter.regodigital.Candidate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentCandidateIdDocBinding


class CandidateIdDoc : Fragment() {
    private var _binding:FragmentCandidateIdDocBinding? = null
    private val binding:FragmentCandidateIdDocBinding
    get() =  _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentCandidateIdDocBinding.inflate(layoutInflater)
        return binding.root
    }


}