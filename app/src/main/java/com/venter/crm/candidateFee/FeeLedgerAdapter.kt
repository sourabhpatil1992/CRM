package com.venter.crm.candidateFee

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutFeetransBinding
import com.venter.crm.models.FeeLedger
import com.venter.crm.utils.Constans


class FeeLedgerAdapter(val cnt: Context,val rcpt:feeRcpt):
    ListAdapter<FeeLedger, FeeLedgerAdapter.CandidateListHolder>(ComparatorDiffUtil()) {

    inner class CandidateListHolder(private val binding: LayoutFeetransBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fee: FeeLedger) {
            try {

               binding.txtDate.text = fee.date
                binding.txtRemark.text = fee.remark
                binding.txtAmt.text = fee.amt.toInt().toString()
                binding.feeLedger.setOnClickListener {
                   // var intent = Intent(cnt, CandidateFeeReceiptActivity::class.java)
                    rcpt.onClick(position)
//                    intent.putExtra("id",candidate.id.toString())
//                    intent.putExtra("fee",candidate.cource_fee.toString())
//                    intent.putExtra("paidFee",paidFee.toString())
//                    intent.putExtra("name",name)
                    //cnt.startActivity(intent)
                }
            }catch (e:Exception)
            {
                Log.d(Constans.TAG,"Error in FeeLedgerAdapter.kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<FeeLedger>() {
        override fun areItemsTheSame(oldItem: FeeLedger, newItem: FeeLedger): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: FeeLedger, newItem: FeeLedger): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateListHolder {
        val binding =
            LayoutFeetransBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateListHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}

interface feeRcpt
{
    fun onClick(rcptId:Int)

}