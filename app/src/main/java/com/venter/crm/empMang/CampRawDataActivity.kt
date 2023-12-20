package com.venter.crm.empMang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.ActivityCampRawDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CampRawDataActivity : AppCompatActivity() {
    private var _binding: ActivityCampRawDataBinding? = null
    private val binding: ActivityCampRawDataBinding
        get() = _binding!!
    private var campId: Int = 0

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: DataListAdapter

    private var dataList: ArrayList<RawDataList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCampRawDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            candidateViewModel.getProsSubConfig()
            candidateViewModel.prosSubTypeDataLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        adapter = DataListAdapter(this, it.data)
                        //Log.d(TAG, it.data.toString())
                    }
                }
            }


            campId = intent.getIntExtra("campId", 0)
            if (campId > 0)
                candidateViewModel.getRawDataOfCamping(campId)

            candidateViewModel.allrawDataListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when (it) {
                    is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(
                        this,
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    is NetworkResult.Success -> {
                        dataList = it.data as ArrayList<RawDataList>
                        setData(dataList)

                    }

                }
            }

            binding.edtSearch.doOnTextChanged { _, _, _, _ ->
                val filterList: ArrayList<RawDataList> = ArrayList()
                val text = binding.edtSearch.text.toString()
                dataList.forEach {
                    if (it.id.toString().contains(text) || it.candidate_name.contains(
                            text,
                            true
                        ) || it.mob_no.contains(text) || it.SourceOfApplication.contains(text)
                    )
                        filterList.add(it)
                }
                
                setData(filterList)
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error in CampRawDataActivity.kt is : ${e.message}")
        }
    }

    private fun setData(data: ArrayList<RawDataList>) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}