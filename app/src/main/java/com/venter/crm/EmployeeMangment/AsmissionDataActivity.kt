package com.venter.crm.EmployeeMangment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.crm.databinding.ActivityAsmissionDataBinding
import com.venter.crm.models.RawDataList
import com.venter.crm.utils.Constans.TAG
import com.venter.crm.utils.NetworkResult
import com.venter.crm.utils.TokenManger
import com.venter.crm.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AsmissionDataActivity : AppCompatActivity(),chkListner {

    private var _binding:ActivityAsmissionDataBinding? = null
    private val binding:ActivityAsmissionDataBinding
    get() = _binding!!

    private lateinit var adapter: RawDataListAdapter

    var rawDataList: ArrayList<RawDataList> = ArrayList()

    @Inject
    lateinit var tokenManger: TokenManger

    private val candidateViewModel by viewModels<CandidateViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAsmissionDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RawDataListAdapter(this,this, empType =  tokenManger.getUserType().toString())

        try {
            candidateViewModel.getAddmissionData()

            candidateViewModel.allrawDataListResLiveData.observe(this){
            binding.progressbar.visibility = View.GONE

            when(it)
            {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error ->{ Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()}
                is NetworkResult.Success ->{
                    var srNo = 1
                    it.data!!.forEach{
                        it.srNo = srNo++
                    }
                    adapter.submitList(it.data)
                    binding.rcCandidate.layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    binding.rcCandidate.adapter = adapter

                }
            }
        }

        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in AsmissionDataActivity.kt is "+e.message)
        }
    }

    override fun chkSelect(candidate: RawDataList, checked: Boolean) {

    }
}