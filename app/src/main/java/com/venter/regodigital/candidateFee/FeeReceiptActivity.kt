package com.venter.regodigital.candidateFee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.venter.regodigital.databinding.ActivityFeeReceiptBinding
import com.venter.regodigital.models.FeeLedger
import com.venter.regodigital.models.FeeLedgerDet
import com.venter.regodigital.utils.Constans.TAG
import com.venter.regodigital.utils.NetworkResult
import com.venter.regodigital.viewModelClass.CandidateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeeReceiptActivity : AppCompatActivity(),feeRcpt {
    private var _binding: ActivityFeeReceiptBinding? = null
    private val binding: ActivityFeeReceiptBinding
        get() = _binding!!

    private val candidateViewModel by viewModels<CandidateViewModel>()

    private lateinit var adapter: FeeLedgerAdapter

    private lateinit var candidateFeeLedger: FeeLedgerDet
    private lateinit var  name:String

    private lateinit var candidateId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFeeReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FeeLedgerAdapter(this,this)
        name = intent.getStringExtra("name").toString()
        candidateId = intent.getStringExtra("id").toString()
        binding.txtName.text = name
        val fee = intent.getIntExtra("fee",0)
        val paidFee  = intent.getIntExtra("paidFee",0)
        binding.txtTotalFee.text = fee.toString()
        binding.txtPaidFee.text = paidFee.toString()
        binding.txtPendingFee.text = (fee-paidFee).toString()
        binding.txtTransReq.text = if(intent.getStringExtra("transReq").toString()=="true")
            "Yes"
        else
            "No"


        candidateViewModel.getCandidateFeeLedger(candidateId)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, CandidateFeeReceiptActivity::class.java)
            intent.putExtra("id", candidateId.toString())
            intent.putExtra("name", name)
            startActivity(intent)
        }

        candidateViewModel.feeLedgerResLiveData.observe(this)
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
                    candidateFeeLedger = it.data!!
                    adapter.submitList(it.data!!.feeLedger)
                    binding.txtNextPayDate.text = it.data.nextPaymentDate
                    binding.rcCandidate.layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    binding.rcCandidate.adapter = adapter
                }
            }
        }

    }

    override fun onClick(rcptId: Int) {
        val intent = Intent(this, CandidateFeeReceiptActivity::class.java)
        intent.putExtra("id", candidateId.toString())
        intent.putExtra("name", name)
        intent.putExtra("rcpt",candidateFeeLedger.feeLedger[rcptId])
        intent.putExtra("nextDate",candidateFeeLedger.nextPaymentDate)
        startActivity(intent)
    }
}