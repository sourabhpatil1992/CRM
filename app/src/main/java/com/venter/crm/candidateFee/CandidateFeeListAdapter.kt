package com.venter.crm.candidateFee

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutCandidatefeeBinding
import com.venter.crm.models.CandidateFeeList
import com.venter.crm.utils.Constans.TAG

class CandidateFeeListAdapter(val cnt: Context):
    ListAdapter<CandidateFeeList, CandidateFeeListAdapter.CandidateListHolder>(ComparatorDiffUtil()) {

    inner class CandidateListHolder(private val binding: LayoutCandidatefeeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: CandidateFeeList) {
            try {
                val name = candidate.first_name+" "+candidate.middel_name+" "+candidate.last_name
                binding.candidateName.text = name
                binding.totalFee.text = candidate.cource_fee!!.toInt().toString()
                binding.transactionFee.text = candidate.transComm!!.toInt().toString()
               val paidFee= if(candidate.paidFee != null)
                  candidate.paidFee!!.toInt()
                else
                    0
                binding.paidFee.text = paidFee.toString()
                binding.pendingFee.text = ((candidate.cource_fee!!.toInt()+candidate.transComm!!.toInt()) - paidFee).toString()
                binding.candidateFee.setOnClickListener {
                   var intent = Intent(cnt, FeeReceiptActivity::class.java)
                    intent.putExtra("id",candidate.id.toString())
                    intent.putExtra("fee",candidate.cource_fee.toInt())
                    intent.putExtra("paidFee",paidFee.toInt())
                    intent.putExtra("transReq",candidate.transReq)
                    intent.putExtra("transComm",candidate.transComm.toInt())
                    intent.putExtra("name",name)
                    cnt.startActivity(intent)
                }
            }catch (e:Exception)
            {
                Log.d(TAG,"Error in CandidateFeeListAdapter.kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<CandidateFeeList>() {
        override fun areItemsTheSame(oldItem: CandidateFeeList, newItem: CandidateFeeList): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: CandidateFeeList, newItem: CandidateFeeList): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateListHolder {
        val binding =
            LayoutCandidatefeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateListHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}