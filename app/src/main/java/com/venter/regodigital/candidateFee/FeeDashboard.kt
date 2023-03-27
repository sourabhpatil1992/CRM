package com.venter.regodigital.candidateFee

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.ActivityFeeDashboardBinding
import com.venter.regodigital.models.CandidateFeeList
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class FeeDashboard : AppCompatActivity()
{
    private var _binding:ActivityFeeDashboardBinding? = null
    private val binding:ActivityFeeDashboardBinding
    get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()
    private lateinit var candidateFeeList: List<CandidateFeeList>
    private lateinit var adapter:CandidateFeeListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFeeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CandidateFeeListAdapter(this)

        candidateViewModel.getCandidateFeeList()

        candidateViewModel.candidateFeeListResLiveData.observe(this)
        {
            binding.progressbar.visibility = View.GONE

            when(it) {
                is NetworkResult.Loading -> binding.progressbar.visibility = View.VISIBLE
                is NetworkResult.Error -> Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                is NetworkResult.Success ->{
                    candidateFeeList = it.data!!
                    getData()
                }
            }
        }

        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            filterAction()

        }

        binding.btnFilter.setOnClickListener {
            binding.linviewFilter.visibility = View.VISIBLE
        }

        binding.txtApply.setOnClickListener {
            binding.linviewFilter.visibility = View.GONE
            filterAction()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setContentView(candidateList: List<CandidateFeeList>) {

        adapter.submitList(candidateList)
       binding.rcCandidate.layoutManager =
           StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
       binding.rcCandidate.adapter = adapter
    }

    private fun getData()
    {
        try {
            binding.linviewBusiness.visibility = View.VISIBLE
            var totalFee = 0
            var paidFee = 0

            candidateFeeList.forEach {
                totalFee += it.cource_fee!!.toInt() + it.transComm!!.toInt()
                paidFee +=
                    if(it.paidFee!= null)
                        it.paidFee!!.toInt()
                else
                    0
            }

            val formatter = DecimalFormat("##,##,##,##,##,###")
            binding.totalFee.text = formatter.format(totalFee)+" /-"
            binding.pendingFee.text = formatter.format(totalFee - paidFee)+" /-"
            setContentView(candidateFeeList)
        }
        catch (e:Exception)
        {
            Log.d(TAG,"Error in FeeDashboard.kt getData() is "+e.message)
        }
    }
    private  fun filterAction()
    {
        if(binding.edtSearch.text.isEmpty()){
            val text = binding.edtSearch.text.toString()
            val candidateList: ArrayList<CandidateFeeList> = ArrayList()
            candidateFeeList.onEach{

                val paidFee:Double = if(it.paidFee != null)
                    it.paidFee
                else
                    0.0
                if(binding.chkComplete.isChecked || binding.chkPending.isChecked || binding.chkReqTran.isChecked)
                {
                    if(binding.chkComplete.isChecked){
                        if ((it.cource_fee!! - paidFee) == 0.0)
                            if(!candidateList.contains(it))
                                candidateList.add(it)
                    }
                    if(binding.chkPending.isChecked)
                    {
                        if ((it.cource_fee!! - paidFee) > 0.0)
                            if(!candidateList.contains(it))
                            candidateList.add(it)
                    }
                    if(binding.chkReqTran.isChecked)
                    {
                        if (it.transReq == "true")
                            if(!candidateList.contains(it))
                            candidateList.add(it)
                    }
                }
            }
            setContentView(candidateList)

        }
        else
        {
            val text = binding.edtSearch.text.toString()
            val candidateList: ArrayList<CandidateFeeList> = ArrayList()
            candidateFeeList.onEach {
                if(it.first_name.contains(text,true) || it.middel_name.contains(text,true) || it.last_name.contains(text,true) || it.cource_fee.toString().contains(text) || it.paidFee.toString().contains(text))
                {

                    val paidFee:Double = if(it.paidFee != null)
                        it.paidFee
                    else
                        0.0
                    if(binding.chkComplete.isChecked || binding.chkPending.isChecked || binding.chkReqTran.isChecked)
                    {
                        if(binding.chkComplete.isChecked){
                            if ((it.cource_fee!! - paidFee) == 0.0)
                                if(!candidateList.contains(it))
                                candidateList.add(it)
                        }
                        if(binding.chkPending.isChecked)
                        {
                            if ((it.cource_fee!! - paidFee) > 0.0)
                                if(!candidateList.contains(it))
                                candidateList.add(it)
                        }
                        if(binding.chkReqTran.isChecked)
                        {
                            if (it.transReq == "true")
                                if(!candidateList.contains(it))
                                candidateList.add(it)
                        }
                    }
                }
            }
            setContentView(candidateList)

        }
    }

}