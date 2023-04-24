package com.venter.regodigital.EmployeeMangment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.ActivityRawDataBinding
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RawDataActivity : AppCompatActivity(),chkListner {
    private var _binding:ActivityRawDataBinding? = null
    private val binding:ActivityRawDataBinding
    get() = _binding!!
    private lateinit var adapter: RawDataListAdapter

    lateinit var rawList:ArrayList<RawDataList>

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rawList = ArrayList()
        adapter = RawDataListAdapter(this,this,true)

        binding.floatingActionButton.setOnClickListener {
           addData()
        }
        
        showData()

        binding.floatingDelButton.setOnClickListener {
            if(rawList.size >0)
            {
                deleteRawData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
    fun addData()
    {
        candidateViewModel.addMultipleRawData()
        candidateViewModel.stringResData.observe(this){
            binding.progressbar.visibility = View.GONE
            when(it){
                is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                is NetworkResult.Success ->{
                    Toast.makeText(this,"Data Added successfully.",Toast.LENGTH_SHORT).show()
                    showData()
                }
            }

        }
    }
    
    private fun showData()
    {
        try{

            candidateViewModel.getAllRawData()
            val dataObserver = candidateViewModel.allrawDataListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it){
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success ->{
                        rawList = (it.data as ArrayList<RawDataList>)!!
                        var srNo = 1
                        rawList.forEach{
                            it.srNo = srNo++
                        }
                        adapter.submitList(rawList)
                        binding.rcCandidate.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcCandidate.adapter = adapter
                      
                    }
                }


            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in RawData.kt showData() is "+e.message)
        }
    }

    private fun deleteRawData()
    {
        try {
            var deleteList :ArrayList<RawDataList> = ArrayList()
            rawList.forEach{
                if(it.selected)
                    deleteList.add(it)

            }

            if(deleteList.size > 0) {
                candidateViewModel.deleteMultipleRawData(deleteList)
                candidateViewModel.stringResData.observe(this) {
                    binding.progressbar.visibility = View.GONE
                    when (it) {
                        is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                        is NetworkResult.Error -> {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                            showData()
                        }
                        is NetworkResult.Success -> {
                            Toast.makeText(this, "Data Removed successfully.", Toast.LENGTH_SHORT)
                                .show()
                            showData()
                        }
                    }
                }
            }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in The RawDataActivity.kt deleteRawData() is "+e.message)
        }
    }
    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

        rawList[(rawList.indexOf(candidate))].selected = checked



        adapter.submitList(rawList)
        binding.rcCandidate.adapter = adapter

        if(rawList.singleOrNull{ it.selected }!!.selected)
        binding.floatingDelButton.visibility = View.VISIBLE
        else
            binding.floatingDelButton.visibility = View.GONE
    }
}