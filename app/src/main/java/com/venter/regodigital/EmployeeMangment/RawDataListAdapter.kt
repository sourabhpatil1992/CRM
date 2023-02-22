package com.venter.regodigital.EmployeeMangment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.regodigital.candidateFee.FeeReceiptActivity
import com.venter.regodigital.databinding.LayoutCandidatefeeBinding
import com.venter.regodigital.databinding.LayoutRawdataBinding
import com.venter.regodigital.models.CandidateFeeList
import com.venter.regodigital.models.RawDataList
import com.venter.regodigital.utils.Constans

class RawDataListAdapter :ListAdapter<RawDataList,RawDataListAdapter.RawListHolder>(ComparatorDiffUtil()) {

    inner class RawListHolder(private val binding: LayoutRawdataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: RawDataList) {
            try {

                binding.txtName.text = candidate.candidate_name
                binding.txtMobNo.text = candidate.mob_no

            }catch (e:Exception)
            {
                Log.d(Constans.TAG,"Error in RawDataListAdapter .kt bind() is "+e.message)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<RawDataList>() {
        override fun areItemsTheSame(oldItem: RawDataList, newItem: RawDataList): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: RawDataList, newItem: RawDataList): Boolean {
           return false
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RawListHolder {
        val binding =
            LayoutRawdataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RawListHolder(binding)
    }

    override fun onBindViewHolder(holder: RawListHolder, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }

}