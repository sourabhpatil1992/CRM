package com.venter.regodigital.officeexpenses

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.regodigital.candidateFee.FeeReceiptActivity
import com.venter.regodigital.databinding.LayoutCandidatefeeBinding
import com.venter.regodigital.databinding.LayoutExpensereportBinding
import com.venter.regodigital.models.CandidateFeeList
import com.venter.regodigital.models.expensesDet
import com.venter.regodigital.utils.Constans

class ExpenseReportAdapter :ListAdapter<expensesDet,ExpenseReportAdapter.ExpenseListHolder>(ComparatorDiffUtil())
{
    inner class ExpenseListHolder(private val binding: LayoutExpensereportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(det: expensesDet) {
            try {

                binding.txtDate.text = det.trans_date
                binding.txtTitle.text = det.tras_type
                binding.txtDesc.text = det.trans_dscr
                binding.txtExpenseType.text = if(det.category=="Credit") "Cr" else "Dr"
                binding.txtAmt.text = det.transAmt.toString()

            }catch (e:Exception)
            {
                Log.d(Constans.TAG,"Error in ExpenseReportAdapter.kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<expensesDet>() {
        override fun areItemsTheSame(oldItem: expensesDet, newItem: expensesDet): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: expensesDet, newItem: expensesDet): Boolean {
            return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListHolder {
       val binding = LayoutExpensereportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ExpenseListHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}