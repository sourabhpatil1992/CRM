package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.FragmentEmpCampRawDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmpCampRawDataFragment(private val campType: String) : Fragment() {
    private var _binding:FragmentEmpCampRawDataBinding? = null
    private val binding:FragmentEmpCampRawDataBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: DataListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentEmpCampRawDataBinding.inflate(layoutInflater)




            binding.root
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmpCampRawDataFragment.kt is : ${e.message}")
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun getData() {
        candidateViewModel.getEmpCampRawData(campType)
        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE

            when(it)
            {
                is NetworkResult.Loading ->{binding.progressbar.visibility = View.VISIBLE}
                is NetworkResult.Error ->{
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()}
                is NetworkResult.Success ->{

                    setData(it.data!!)
                }
            }
        }
    }

    private fun setData(data: List<RawDataList>) {
        try {
            if (data.isNotEmpty()) {
                var srNo = 1
                data.forEach {
                    it.srNo = srNo++
                }
            }
            adapter.submitList(null)
            adapter.submitList(data)
            binding.rcCandidate.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            binding.rcCandidate.adapter = adapter
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in EmpCampRawDataFragment.kt setData() is : ${e.message}")
        }

    }

    override fun onResume() {
        super.onResume()
        adapter = DataListAdapter(requireContext())



        getData()
    }

}