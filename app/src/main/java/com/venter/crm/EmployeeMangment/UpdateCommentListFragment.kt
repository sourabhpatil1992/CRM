package com.venter.crm.EmployeeMangment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.venter.crm.databinding.FragmentUpdateCommentListBinding
import com.venter.crm.empMang.rawDataCommentAdapter
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateCommentListFragment : Fragment() {
   private var _binding:FragmentUpdateCommentListBinding? = null
    private val binding:FragmentUpdateCommentListBinding
        get() = _binding!!

    private lateinit var adapter : rawDataCommentAdapter

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentUpdateCommentListBinding.inflate(layoutInflater)




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}