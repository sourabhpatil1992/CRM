package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentEmpRawDataBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmpRawDataFragment : Fragment() {

    private var _binding: FragmentEmpRawDataBinding? = null
    private val binding: FragmentEmpRawDataBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter

    private lateinit var act: EmpDashboard
    var rawDataList: ArrayList<RawDataList> =ArrayList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as EmpDashboard
       // rawData = act.rawData

        act.rawData!!.forEach { it->

            if(it.prospect_type.isNullOrEmpty())
                rawDataList.add(it)

        }
        showData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmpRawDataBinding.inflate(layoutInflater)

        adapter = RawDataListAdapter(requireContext())

        /* binding.floatingActionButton.setOnClickListener {
             addData()
         }
 */
        binding.edtSearch.doOnTextChanged { _, _, _, _ ->
           searchList()
        }


        return binding.root
    }

    private fun searchList() {
        if(rawDataList.isNotEmpty() && binding.edtSearch.text.isNotEmpty())
        {
            val text = binding.edtSearch.text.toString()
            var rawData: ArrayList<RawDataList> =ArrayList()
            rawDataList.forEach { it ->
                if(it.candidate_name.contains(text,true) || it.mob_no.contains(text) || it.prospect_type.contains(text,true))
                    rawData.add(it)
            }
            adapter.submitList(rawData)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter
        }
    }

    private fun showData() {
        if (rawDataList.isNotEmpty()) {

            adapter.submitList(rawDataList)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter
        }
        else{
            Toast.makeText(context,"Data not found!!!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}