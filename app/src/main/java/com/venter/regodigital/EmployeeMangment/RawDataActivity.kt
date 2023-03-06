package com.venter.regodigital.EmployeeMangment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.candidateFee.CandidateFeeListAdapter
import com.venter.regodigital.databinding.ActivityRawDataBinding
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RawDataActivity : AppCompatActivity() {
    private var _binding:ActivityRawDataBinding? = null
    private val binding:ActivityRawDataBinding
    get() = _binding!!
    private lateinit var adapter: RawDataListAdapter

    private val candidateViewModel by viewModels<CandidateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRawDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RawDataListAdapter(this)

        binding.floatingActionButton.setOnClickListener {
           addData()
        }
        
        showData()
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
                    //candidateViewModel.stringResData.removeObservers(this)
                }
            }
        }
    }
    
    private fun showData()
    {
        try{

            candidateViewModel.getAllRawData()
            candidateViewModel.allrawDataListResLiveData.observe(this)
            {
                binding.progressbar.visibility = View.GONE
                when(it){
                    is NetworkResult.Loading ->binding.progressbar.visibility = View.VISIBLE
                    is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    is NetworkResult.Success ->{
                        adapter.submitList(it.data!!)
                        binding.rcCandidate.layoutManager =
                            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                        binding.rcCandidate.adapter = adapter
                      
                    }
                }
                //candidateViewModel.allrawDataListResLiveData.removeObservers(this)

            }
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in RawData.kt showData() is "+e.message)
        }
    }
}