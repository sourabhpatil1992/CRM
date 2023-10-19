package com.venter.crm.empMang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.EmployeeMangment.RawDataListAdapter
import com.venter.crm.EmployeeMangment.chkListner
import com.venter.crm.databinding.FragmentColdDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ColdDataFragment : Fragment() {
    private var _binding: FragmentColdDataBinding? = null
    private val binding: FragmentColdDataBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private lateinit var adapter: DataListAdapter

    private var offset = 0


    private var isLoading = false
    private var isLastPage = false


    private var srNo = 1

    @Inject
    lateinit var tokenManger: TokenManger

    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentColdDataBinding.inflate(layoutInflater)

        layoutManager = LinearLayoutManager(context)


        adapter = DataListAdapter(requireContext())


        getData()

        binding.floatingRefreshButton.setOnClickListener {
            refreshData()
        }




        return binding.root
    }


    private fun refreshData() {
        offset = 0
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    private fun getData() {

        candidateViewModel.getColdData(offset)

        candidateViewModel.allrawDataListResLiveData.observe(viewLifecycleOwner)
        {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Success -> setData(it.data!!)
                is NetworkResult.Error -> Toast.makeText(
                    context,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun setData(data: List<RawDataList>) {
        val layoutManager = LinearLayoutManager(context)
        if(data.size<100)
            isLastPage = true
        if (data.size > 0) {

            data.forEach {
                it.srNo = srNo++
            }


            //adapter.submitList(null)
            adapter.notifyDataSetChanged()
            adapter.submitList(data)

            binding.rcCandidate.layoutManager = layoutManager
            binding.rcCandidate.adapter = adapter
            setRescyclerView()
        }



    }

    private fun setRescyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rcCandidate.layoutManager = layoutManager
        binding.rcCandidate.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()


                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {

                        // Load more data here
                        offset +=100
                        candidateViewModel.getAllRawData(offset)
                        getData()
                    }
                }

            }
        })
    }



}