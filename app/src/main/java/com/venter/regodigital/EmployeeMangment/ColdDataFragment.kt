package com.venter.regodigital.EmployeeMangment

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.R
import com.venter.regodigital.databinding.FragmentColdDataBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.utils.TokenManger
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ColdDataFragment : Fragment(), chkListner {
    private var _binding: FragmentColdDataBinding? = null
    private val binding: FragmentColdDataBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private lateinit var adapter: RawDataListAdapter

    private var offset = 0
    private lateinit var dataList: ArrayList<RawDataList>

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1

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

        dataList = ArrayList()
        adapter = RawDataListAdapter(requireContext(), this, empType = tokenManger.getUserType().toString())


        getData()

        binding.floatingRefreshButton.setOnClickListener {
            refreshData()
        }




        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataList = ArrayList()
    }

    private fun refreshData() {
        offset = 0
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

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
        if (data.size > 0) {
            var cnt = dataList.size

            data.forEach {
                it.srNo = srNo++
                dataList.add(it)
            }
           // data.map {  }

            adapter.submitList(null)
            adapter.notifyDataSetChanged()
            adapter.submitList(dataList)

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