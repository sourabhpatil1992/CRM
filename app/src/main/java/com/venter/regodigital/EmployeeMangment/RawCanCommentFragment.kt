package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentRawCanCommentBinding
import com.venter.regodigital.models.RawCandidateData
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.TokenManger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RawCanCommentFragment : Fragment() {

    @Inject
    lateinit var tokenManger: TokenManger

    private var _binding:FragmentRawCanCommentBinding? =null
    private val binding:FragmentRawCanCommentBinding
    get()  = _binding!!
    private lateinit var act: RawDataDetActivity

    private lateinit var adapter : rawDataCommentAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as RawDataDetActivity
        setView(act.data!!)
    }

    private fun setView(data: RawCandidateData) {
        adapter.submitList(data.commentList)
        binding.rcViewComments.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rcViewComments.adapter = adapter


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentRawCanCommentBinding.inflate(layoutInflater)

        adapter = rawDataCommentAdapter(
            tokenManger.getUserId().toString())
        return binding.root
    }

}