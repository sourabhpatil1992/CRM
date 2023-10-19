package com.venter.crm.empMang

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.FragmentRawDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RawDataFragment : Fragment() {

    private var _binding:FragmentRawDataBinding? = null
    private val binding:FragmentRawDataBinding
        get() = _binding!!

    @Inject
    lateinit var tokenManger: TokenManger

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: DataListAdapter





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRawDataBinding.inflate(layoutInflater)

        //Set The Adapter
        adapter = DataListAdapter(requireContext())

        //FetCh data from the Server
        candidateViewModel.getEmpRawData()
        getData()

        //Get Data on the search box typing set the delay of 1 sec for the search
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            Handler().postDelayed({
                if ( binding.edtSearch.text!!.isEmpty()) {

                    candidateViewModel.getEmpRawData()


                } else {
                    candidateViewModel.getEmpSearchData(text.toString())

                }},1000)


        }


        binding.swiperefresh.setOnRefreshListener {
            candidateViewModel.getEmpRawData()

        }



        return binding.root
    }

    private fun getData() {
        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            binding.swiperefresh.isRefreshing =  false
            when(it)
            {
                is NetworkResult.Loading ->{binding.progressbar.visibility = View.VISIBLE}
                is NetworkResult.Error ->{Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_LONG).show()}
                is NetworkResult.Success ->{

                    setData(it.data!!)
                }
            }
        }
    }

    private fun setData(data: List<RawDataList>)
    {
        try{
            if (data.isNotEmpty()) {
                var srNo = 1
                data.forEach {
                    it.srNo = srNo++
                }
            }
                adapter.submitList(data)
                binding.rcCandidate.layoutManager =
                    StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                binding.rcCandidate.adapter = adapter
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in RawDataFragment.kt setData() is : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}