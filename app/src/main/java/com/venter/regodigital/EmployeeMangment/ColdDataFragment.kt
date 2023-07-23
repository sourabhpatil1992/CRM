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

    private var isScrolled: Boolean = false

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

        binding.rcCandidate.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolled = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


            }
        })




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
                cnt++
                it.srNo = cnt
                dataList.add(it)
            }

            adapter.submitList(dataList)

            binding.rcCandidate.layoutManager = layoutManager
            binding.rcCandidate.adapter = adapter
        }


    }


}