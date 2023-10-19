package com.venter.crm.userledger

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venter.crm.databinding.LayoutCalllogBinding
import com.venter.crm.models.UserReportData
import com.venter.crm.utils.Constans.TAG

class UserReportAdapter:ListAdapter<UserReportData,UserReportAdapter.ReportBinding>(ComparatorDiffUtil()) {

    inner class ReportBinding(val binding:LayoutCalllogBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(report:UserReportData){
            try {
                binding.candidateName.text = report.candidate_name
                binding.comment.text = report.remark

                binding.prospectType.text = report.prosType
                binding.commentAt.text = report.created_on
                val hours = report.callTime / 3600
                val minutes = (report.callTime % 3600) / 60
                val remainingSeconds = report.callTime % 60

                binding.callTime.text =  String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)

            }
            catch (e:Exception)
            {
                Log.d(TAG,"Error in UserReportAdapter.kt bind() is ${e.message}")
            }

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<UserReportData>() {
        override fun areItemsTheSame(oldItem: UserReportData, newItem: UserReportData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserReportData, newItem: UserReportData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportBinding {
        val binding =
            LayoutCalllogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportBinding(binding)
    }

    override fun onBindViewHolder(holder: ReportBinding, position: Int) {
        val temp = getItem(position)
        holder.bind(temp)
    }
}