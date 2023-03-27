package com.venter.regodigital.EmployeeMangment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.Dashboard.EmpDashboard
import com.venter.regodigital.databinding.FragmentEmpFolloupCandBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Get The Today's Date
 * Use the to check Follow up of Candidate List

 **/

@AndroidEntryPoint
class EmpFolloupCandFragment : Fragment(),chkListner {

    private var _binding: FragmentEmpFolloupCandBinding? = null
    private val binding: FragmentEmpFolloupCandBinding
        get() = _binding!!

    private lateinit var adapter: RawDataListAdapter

    //private lateinit var act: EmpDashboard
    lateinit var rawDataList: ArrayList<RawDataList>
    lateinit var followList: List<Int>

    private val candidateViewModel by viewModels<CandidateViewModel>()

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        act = activity as EmpDashboard
        rawDataList = ArrayList()

        act.rawData!!.forEach { it ->
            rawDataList.add(it)
        }
        showData()

    }*/

    private fun showData() {
        try {
            if (followList.isNotEmpty() && rawDataList.isNotEmpty()) {
                var rawList: ArrayList<RawDataList> = ArrayList()
                rawDataList.forEach{
                    if(followList.contains(it.id))
                        rawList.add(it)
                }
                adapter.submitList(rawList)
                binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                binding.rcCandidate.adapter = adapter
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error in EmpFolloupCandFragment.kt showData() is " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create Binding
        _binding = FragmentEmpFolloupCandBinding.inflate(layoutInflater)
        adapter = RawDataListAdapter(requireContext(),this)
        rawDataList = ArrayList()
        followList = ArrayList()
        candidateViewModel.getFollowUpList()
        candidateViewModel.intListResData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    .show()
                is NetworkResult.Success -> {
                    followList = it.data!!
                    showData()
                }
            }
        }

        candidateViewModel.getEmpRawData()
        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when(it){
                is NetworkResult.Loading ->{
                    binding.progressbar.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success ->{
                    it.data!!.forEach { it ->
                        rawDataList.add(it)
                    }
                    showData()

                }
            }


        }



        return binding.root
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }


}